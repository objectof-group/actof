package net.objectof.actof.repospy.migration;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.objectof.actof.repospy.migration.impl.RuleBuilder;
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
            port(id, fromTx, toTx);
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

    private void port(Id<?> fromId, Transaction fromTx, Transaction toTx) {

        // Archetype archetype = fromId.kind().getStereotype().getModel();
        Stereotype st = fromId.kind().getStereotype();
        switch (st) {
            case COMPOSED:
                portComposite(fromId, fromTx, toTx);
                break;
            case INDEXED:
            case MAPPED:
            case SET:
                portAggregate(fromId, fromTx, toTx);
                break;
            default:
                throw new UnsupportedOperationException();
        }

    }

    private void portComposite(Id<?> fromId, Transaction fromTx, Transaction toTx) {

        Resource<Aggregate<Object, Object>> fromComposite = fromTx.retrieve(fromId);
        Resource<Aggregate<Object, Object>> toComposite = toTx.retrieve(idmap.get(fromId));

        // aggregates (non-composed) only have 1 part describing the contents
        for (Kind<?> childKind : fromComposite.id().kind().getParts()) {

            Object oldKey = childKind.getComponentName();
            Object oldValue = fromComposite.value().get(unqualify(oldKey, true));
            PorterContext context = new PorterContext(oldKey, oldValue, childKind, fromTx, toTx);

            // calls from here pass qualified=true, since composites use
            // names like Entity.field that need to be parsed
            if (childKind.getStereotype().getModel() == Archetype.CONTAINER) {
                portContainer(context, toComposite.value(), true);
            } else if (childKind.getStereotype() == Stereotype.REF) {
                portReference(context, toComposite.value(), true);
            } else {
                portLeaf(context, toComposite.value(), true);
            }
        }
    }

    private void portAggregate(Id<?> fromId, Transaction fromTx, Transaction toTx) {

        Resource<Aggregate<Object, Object>> fromAggr = fromTx.retrieve(fromId);
        Resource<Aggregate<Object, Object>> toAggr = toTx.retrieve(idmap.get(fromId));

        // aggregates (non-composed) only have 1 part describing the contents
        Kind<?> childKind = fromAggr.id().kind().getParts().get(0);

        for (Object oldKey : fromAggr.value().keySet()) {
            Object oldValue = fromAggr.value().get(oldKey);
            PorterContext context = new PorterContext(oldKey, oldValue, childKind, fromTx, toTx);

            // calls from here pass qualified=false, since aggregatges don't use
            // names like Entity.field that need to be parsed
            if (childKind.getStereotype().getModel() == Archetype.CONTAINER) {
                portContainer(context, toAggr.value(), false);
            } else if (childKind.getStereotype() == Stereotype.REF) {
                portReference(context, toAggr.value(), false);
            } else {
                portLeaf(context, toAggr.value(), false);
            }
        }
    }

    private void portContainer(PorterContext context, Aggregate<Object, Object> toParent, boolean qualified) {
        Resource<?> oldRef = (Resource<?>) context.getValue();
        Object newKey = Rule.transformKey(rules, context);

        // get the kind for the new key. Trivial case is that it's still a
        // container, but if we're reducing a list to it's first entry, for
        // example, we need to take different steps
        Kind<?> newKind = kindFromKey(context, newKey.toString());

        if (newKind.getStereotype().getModel() == Archetype.CONTAINER) {
            // if it's still a container, create it first, then recurse into it
            Resource<?> newRes = context.getToTx().create(newKey.toString());
            idmap.put(oldRef.id(), newRes.id());
            toParent.set(unqualify(newKey, qualified), newRes);
            port(oldRef.id(), context.getFromTx(), context.getToTx());
        } else if (newKind.getStereotype() == Stereotype.REF) {
            // there's a chance that the user passed us a reference to something
            // in the old repo.
            referenceJobs.add(() -> {
                Resource<Object> newRef = (Resource<Object>) Rule.transformValue(rules, context);
                // just incase this is a ref to the old package
                    newRef = updateRef(context, newRef);
                    toParent.set(unqualify(newKey, qualified), newRef);
                });
        } else {
            // otherwise, just rely on rules to port value
            portLeaf(context, toParent, qualified);
        }

    }

    private void portReference(PorterContext context, Aggregate<Object, Object> toParent, boolean qualified) {
        referenceJobs.add(() -> {

            Object newKey = Rule.transformKey(rules, context);

            // get the old value as a resource so we can look up its id and
            // find the new id in the idmap
                Resource<Object> oldRef = (Resource<Object>) context.getValue();
                if (oldRef == null) { return; }
                Object newValue = updateRef(context, oldRef);

                // create a new context where the value is not the old
                // reference, but
                // the old reference as ported into the new repo
                PorterContext refContext = context.copy().setValue(newValue);
                newValue = Rule.transformValue(rules, refContext);
                toParent.set(unqualify(newKey, qualified), newValue);
            });
    }

    private Resource<Object> updateRef(PorterContext context, Resource<Object> ref) {
        if (ref.tx().getPackage().equals(context.getFromTx().getPackage())) {
            Id<?> oldId = ref.id();
            // look up the reference target in the new repo
            Id<?> newId = idmap.get(oldId);
            Resource<Object> newValue = context.getToTx().retrieve(newId);
            return newValue;
        } else {
            return ref;
        }
    }

    private void portLeaf(PorterContext context, Aggregate<Object, Object> newParent, boolean qualified) {
        Object newKey = Rule.transformKey(rules, context);
        Object newValue = Rule.transformValue(rules, context);
        newParent.set(unqualify(newKey, qualified), newValue);
    }

    private Object unqualify(Object key, boolean qualified) {
        if (!qualified) { return key; }
        if (!(key instanceof String)) { return key; }
        String keyString = key.toString();
        int lastIndex = keyString.lastIndexOf('.');
        if (lastIndex == -1) { return key; }
        return keyString.substring(lastIndex + 1);
    }

    private Kind<?> kindFromKey(PorterContext context, String key) {
        return kindFromKey(context.getToTx().getPackage().getParts(), key);
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
