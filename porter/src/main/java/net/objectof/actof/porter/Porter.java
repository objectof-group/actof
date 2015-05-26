package net.objectof.actof.porter;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.objectof.actof.porter.impl.RuleBuilder;
import net.objectof.aggr.Aggregate;
import net.objectof.aggr.Listing;
import net.objectof.connector.Connector;
import net.objectof.connector.Connector.Initialize;
import net.objectof.connector.ConnectorException;
import net.objectof.connector.sql.ISQLiteConnector;
import net.objectof.ext.Archetype;
import net.objectof.model.Id;
import net.objectof.model.Kind;
import net.objectof.model.Package;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;
import net.objectof.model.Transaction;


public class Porter {

    private List<Rule> rules = new ArrayList<>();
    private Map<Id<?>, Id<?>> idmap = new HashMap<>();

    // we need to make sure that when references are connected, everything
    // already exists in the new repo, so when we walk the repo tree, we store
    // all reference port operations as Runnable jobs to be run later
    private List<Runnable> referenceJobs = new ArrayList<>();

    public Porter() {}

    public Porter(Rule... rule) {
        rules.addAll(Arrays.asList(rule));
    }

    public void port(Package from, Package to) {

        idmap.clear();
        Transaction fromTx = from.connect(getClass());
        Transaction toTx = to.connect(getClass());
        for (Kind<?> kind : from.getParts()) {

            // skip non-entities
            if (kind.getPartOf() != null) {
                continue;
            }

            Iterable<Resource<?>> resources = fromTx.enumerate(kind.getComponentName());
            for (Resource<?> oldResource : resources) {
                Object oldKey = kind.getComponentName();
                PorterContext context = new PorterContext(oldKey, oldResource, oldResource.id().kind(), fromTx, toTx);
                Object newKey = Rule.transformKey(rules, context);
                Resource<?> newResource = toTx.create(newKey.toString());
                idmap.put(oldResource.id(), newResource.id());
            }
        }

        toTx.post();
        toTx = to.connect(getClass());

        for (Id<?> id : new HashSet<>(idmap.keySet())) {
            walkContainer(id, fromTx, toTx);
        }

        // reference porting is done last so that everything else is in place
        for (Runnable r : referenceJobs) {
            r.run();
        }

        toTx.post();

    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public void setRules(Rule... rules) {
        this.rules.clear();
        this.rules.addAll(Arrays.asList(rules));
    }

    /**
     * Ports the elements of a container
     * 
     * @param fromId
     *            the Id of the container resource in the source repo
     * @param fromTx
     *            a transaction for the source repo
     * @param toTx
     *            a transaction for the destination repo
     */
    private void walkContainer(Id<?> fromId, Transaction fromTx, Transaction toTx) {

        // Archetype archetype = fromId.kind().getStereotype().getModel();
        Stereotype st = fromId.kind().getStereotype();
        switch (st) {
            case COMPOSED:
                walkComposite(fromId, fromTx, toTx);
                break;
            case INDEXED:
            case MAPPED:
            case SET:
                walkAggregate(fromId, fromTx, toTx);
                break;
            default:
                throw new UnsupportedOperationException();
        }

    }

    private void walkComposite(Id<?> fromId, Transaction fromTx, Transaction toTx) {

        Resource<Aggregate<Object, Object>> fromComposite = fromTx.retrieve(fromId);
        Resource<Aggregate<Object, Object>> toComposite = toTx.retrieve(idmap.get(fromId));

        // composed entities have different kinds for each field
        for (Kind<?> childKind : fromComposite.id().kind().getParts()) {

            // build context
            Object oldKey = childKind.getComponentName();
            Object oldValue = fromComposite.value().get(unqualify(oldKey, true));
            PorterContext context = new PorterContext(oldKey, oldValue, childKind, fromTx, toTx);

            // calls from here pass qualified=true, since composites use
            // names like Entity.field that need to be parsed
            if (childKind.getStereotype().getModel() == Archetype.CONTAINER) {
                portContainer(context, toComposite.value(), true);
            } else {
                portLeaf(context, toComposite.value(), true);
            }
        }
    }

    private void walkAggregate(Id<?> fromId, Transaction fromTx, Transaction toTx) {

        // this aggregate doesn't exist in new repo
        if (!idmap.containsKey(fromId)) { return; }

        Resource<Aggregate<Object, Object>> fromAggr = fromTx.retrieve(fromId);
        Resource<Aggregate<Object, Object>> toAggr = toTx.retrieve(idmap.get(fromId));

        // aggregates (non-composed) only have 1 part describing the contents
        Kind<?> childKind = fromAggr.id().kind().getParts().get(0);

        for (Object oldKey : fromAggr.value().keySet()) {

            // build context
            Object oldValue = fromAggr.value().get(oldKey);
            PorterContext context = new PorterContext(oldKey, oldValue, childKind, fromTx, toTx);

            // calls from here pass qualified=false, since aggregatges don't use
            // names like Entity.field that need to be parsed
            if (childKind.getStereotype().getModel() == Archetype.CONTAINER) {
                portContainer(context, toAggr.value(), false);
            } else {
                portLeaf(context, toAggr.value(), false);
            }
        }
    }

    private void portContainer(PorterContext context, Aggregate<Object, Object> toParent, boolean qualified) {

        PorterContext ported = transform(context);

        System.out.println("Porting " + context.getKey());

        if (ported.getKind().getStereotype() == Stereotype.REF) {
            // there's a chance that the user passed us a reference to something
            // in the old repo.
            referenceJobs.add(() -> {
                toParent.set(unqualify(ported.getKey(), qualified), ported.getValue());
            });
        } else {
            toParent.set(unqualify(ported.getKey(), qualified), ported.getValue());
        }

        // recurse into the old container's fields/elements
        if (context.getKind().getStereotype().getModel() == Archetype.CONTAINER) {
            Resource<?> res = (Resource<?>) context.getValue();
            if (res == null) { return; }
            walkContainer(res.id(), context.getFromTx(), context.getToTx());
        }

    }

    /**
     * Ports anything which isn't a container, and so doesn't require any
     * recursion
     * 
     * @param context
     *            the context of this item
     * @param toParent
     *            parent aggregate of this leaf
     * @param qualified
     *            if keys are qualified strings which must be parsed (as in
     *            compositions), or simple, as in aggregates like indexed.
     */
    private void portLeaf(PorterContext context, Aggregate<Object, Object> toParent, boolean qualified) {
        PorterContext ported = transform(context);
        toParent.set(unqualify(ported.getKey(), qualified), ported.getValue());
    }

    /**
     * Accepts a PorterContext containing the source key/value/kind and returns
     * a PorterContext containing the transformed key/value/kind
     * 
     * @param context
     *            the source context
     * @return a context containing the transformed value
     */
    private PorterContext transform(PorterContext context) {
        PorterContext result = context.copy();

        // key
        Object newKey = Rule.transformKey(rules, context);
        result.setKey(newKey);

        // kind
        Kind<?> kind = kindFromKey(context.getToTx(), newKey.toString());
        result.setKind(kind);

        // value - not necessarily a reference, or even a resource
        Object newValue = Rule.transformValue(rules, context);
        newValue = updateRef(context, newValue);
        result.setValue(newValue);

        return result;
    }

    private void remember(PorterContext fromContext, PorterContext toContext) {
        remember(fromContext.getValue(), toContext.getValue());
    }

    private void remember(Object oFrom, Object oTo) {
        Resource<?> from = (Resource<?>) oFrom;
        Resource<?> to = (Resource<?>) oTo;
        idmap.put(from.id(), to.id());
    }

    /**
     * Checks if the given object is a resource from the old repo. If it is, it
     * looks up the corresponding resource in the new repo in the idmap, and
     * returns it. This is useful for when a user-generated rule returns a value
     * from the old repo.
     * 
     * The PorterContext supplied to this method shold have a ported
     * {@link Kind}
     * 
     * @param context
     * @param ref
     * @return
     */
    private Object updateRef(PorterContext context, Object object) {

        System.out.println("updateRef: " + object);

        // if (context.getKind().getStereotype() != Stereotype.REF) { return
        // object; }
        if (!(object instanceof Resource)) { return object; }

        Resource<Object> res = (Resource<Object>) object;
        if (res.tx().getPackage().equals(context.getToTx().getPackage())) { return object; }

        Id<?> oldId = res.id();
        if (!idmap.containsKey(oldId)) {
            //
            Resource<Object> newValue = context.getToTx().create(context.getKind().getComponentName());
            idmap.put(oldId, newValue.id());
            return newValue;
        } else {
            // look up the reference target in the new repo
            Id<?> newId = idmap.get(oldId);
            Resource<Object> newValue = context.getToTx().retrieve(newId);
            return newValue;
        }

    }

    private boolean isContainer(Kind<?> kind) {
        return kind.getStereotype().getModel() == Archetype.CONTAINER;
    }

    private Object unqualify(Object key, boolean qualified) {
        if (!qualified) { return key; }
        if (!(key instanceof String)) { return key; }
        String keyString = key.toString();
        int lastIndex = keyString.lastIndexOf('.');
        if (lastIndex == -1) { return key; }
        return keyString.substring(lastIndex + 1);
    }

    private Kind<?> kindFromKey(Transaction toTx, String key) {
        return kindFromKey(toTx.getPackage().getParts(), key);
    }

    private Kind<?> kindFromKey(Iterable<? extends Kind<?>> kinds, String key) {
        for (Kind<?> kind : kinds) {
            if (kind.getComponentName().equals(key)) { return kind; }
            if (key.startsWith(kind.getComponentName())) { return kindFromKey(kind.getParts(), key); }
        }
        return null;
    }

    public static void main(String[] args) throws ConnectorException, FileNotFoundException {
        // testRealm();
        // testRulePrinting();
        testRealmReversed();
    }

    private static void testSettings() throws ConnectorException, FileNotFoundException {
        new File("/home/nathaniel/Desktop/Porting/settings/quotes-migrate.db").delete();

        // old package
        Connector oldConnector = new ISQLiteConnector();
        oldConnector.setParameter(ISQLiteConnector.KEY_FILENAME, "/home/nathaniel/Desktop/Porting/settings/quotes.db");
        oldConnector.setParameter(ISQLiteConnector.KEY_REPOSITORY, "example.com:1520/quotes");
        Package oldRepo = oldConnector.getPackage();

        // new package
        Connector newConnector = new ISQLiteConnector();
        newConnector.setParameter(ISQLiteConnector.KEY_FILENAME,
                "/home/nathaniel/Desktop/Porting/settings/quotes-migrate.db");
        newConnector.setParameter(ISQLiteConnector.KEY_REPOSITORY, "example.com:1520/quotes");
        Package newRepo = newConnector.createPackage(new FileInputStream(
                "/home/nathaniel/Desktop/Porting/settings/settings-schema-migrate.xml"), Initialize.WHEN_EMPTY);

        // @formatter:off
        
        Rule settings = RuleBuilder.start()
                .forKey("Setting")
                .setKey("Preference")
                .build();
        
        Rule settingkey = RuleBuilder.start()
                .forKey("Setting.key")
                .setKey("Preference.name")
                .build();
        
        Rule append = RuleBuilder.start()
                .forStereotype(Stereotype.TEXT)
                .valueTransform((context) -> context.getValue().toString() + "...")
                .build();
        
        // @formatter:on

        Porter p = new Porter(settings, settingkey, append);
        p.port(oldRepo, newRepo);

    }

    private static void testRealm() throws ConnectorException, FileNotFoundException {
        new File("/home/nathaniel/Desktop/Porting/emptyapp/empty-port.db").delete();

        // old package
        Connector oldConnector = new ISQLiteConnector();
        oldConnector.setParameter(ISQLiteConnector.KEY_FILENAME, "/home/nathaniel/Desktop/Porting/emptyapp/empty.db");
        oldConnector.setParameter(ISQLiteConnector.KEY_REPOSITORY, "realmproject.net:1502/realm");
        Package oldRepo = oldConnector.getPackage();

        // new package
        Connector newConnector = new ISQLiteConnector();
        newConnector.setParameter(ISQLiteConnector.KEY_FILENAME,
                "/home/nathaniel/Desktop/Porting/emptyapp/empty-port.db");
        newConnector.setParameter(ISQLiteConnector.KEY_REPOSITORY, "realmproject.net:1502/realm");
        Package newRepo = newConnector.createPackage(new FileInputStream(
                "/home/nathaniel/Desktop/Porting/emptyapp/realm-port.xml"), Initialize.WHEN_EMPTY);

        // @formatter:off
        
        Rule roleToRoles = RuleBuilder.start()
            .forKey("Person.role")
            .setKey("Person.roles")
            .valueTransform((context) -> {
                Listing<Object> roles = context.getToTx().create("Person.roles");
                roles.add(context.getValue());
                return roles;
            })
            .build();
        
        
//        Rule settings = RuleBuilder.start()
//                .forKey("Setting")
//                .setKey("Preference")
//                .build();
//        
//        Rule settingkey = RuleBuilder.start()
//                .forKey("Setting.key")
//                .setKey("Preference.name")
//                .build();
//        
//        Rule append = RuleBuilder.start()
//                .forStereotype(Stereotype.TEXT)
//                .valueTransform((k, v, kind) -> v.toString() + "...")
//                .build();
        
        // @formatter:on

        Porter p = new Porter(roleToRoles);

        System.out.println("-----------------------------");

        p.port(oldRepo, newRepo);

    }

    private static void testRealmReversed() throws ConnectorException, FileNotFoundException {
        new File("/home/nathaniel/Desktop/Porting/rolereversal/empty-port.db").delete();

        // old package
        Connector oldConnector = new ISQLiteConnector();
        oldConnector.setParameter(ISQLiteConnector.KEY_FILENAME,
                "/home/nathaniel/Desktop/Porting/rolereversal/empty.db");
        oldConnector.setParameter(ISQLiteConnector.KEY_REPOSITORY, "realmproject.net:1502/realm");
        Package oldRepo = oldConnector.getPackage();

        // new package
        Connector newConnector = new ISQLiteConnector();
        newConnector.setParameter(ISQLiteConnector.KEY_FILENAME,
                "/home/nathaniel/Desktop/Porting/rolereversal/empty-port.db");
        newConnector.setParameter(ISQLiteConnector.KEY_REPOSITORY, "realmproject.net:1502/realm");
        Package newRepo = newConnector.createPackage(new FileInputStream(
                "/home/nathaniel/Desktop/Porting/rolereversal/realm-port.xml"), Initialize.WHEN_EMPTY);

        // @formatter:off
        
        Rule rolesToRole = RuleBuilder.start()
            .forKey("Person.roles")
            .setKey("Person.role")
            .valueTransform(context -> {
                Listing<Object> roles = (Listing<Object>) context.getValue();
                if (roles.size() == 0) { return null; }
                return roles.get(0);
            })
            .build();
        
//        Rule roleToRoles = RuleBuilder.start()
//            .forKey("Person.role")
//            .setKey("Person.roles")
//            .valueTransform((context) -> {
//                Listing<Object> roles = context.getToTx().create("Person.roles");
//                roles.add(context.getValue());
//                return roles;
//            })
//            .build();
        
        
//        Rule settings = RuleBuilder.start()
//                .forKey("Setting")
//                .setKey("Preference")
//                .build();
//        
//        Rule settingkey = RuleBuilder.start()
//                .forKey("Setting.key")
//                .setKey("Preference.name")
//                .build();
//        
//        Rule append = RuleBuilder.start()
//                .forStereotype(Stereotype.TEXT)
//                .valueTransform((k, v, kind) -> v.toString() + "...")
//                .build();
        
        // @formatter:on

        Porter p = new Porter(rolesToRole);

        System.out.println("-----------------------------");

        p.port(oldRepo, newRepo);

    }

    private static void testRulePrinting() {
        // @formatter:off
        Rule testRule = RuleBuilder.start()
                .forKey("asdf")
                .forKey("qwerty")
                .setKey("asdf++")
                .match(context -> true)
                .keyTransform(context -> context.getKey())
                .valueTransform(context -> context.getValue())
                .build();
        System.out.println(testRule);
        // @formatter:on
    }
}
