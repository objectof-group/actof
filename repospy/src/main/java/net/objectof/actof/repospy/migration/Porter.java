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
import net.objectof.actof.repospy.migration.rulecomponents.RuleContext;
import net.objectof.aggr.Aggregate;
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
    Map<Id<?>, Id<?>> idmap = new HashMap<>();

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
                RuleContext context = new RuleContext(oldKey, oldResource, oldResource.id().kind(), fromTx, toTx);
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

        System.out.println("Porting " + fromId);

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

        System.out.println("Porting " + fromId + " as Composite");

        Resource<Aggregate<Object, Object>> fromComposite = fromTx.retrieve(fromId);
        Resource<Aggregate<Object, Object>> toComposite = toTx.retrieve(idmap.get(fromId));
        // aggregates (non-composed) only have 1 part describing the contents

        for (Kind<?> childKind : fromComposite.id().kind().getParts()) {

            System.out.println("Porting " + childKind.getComponentName());
            Object oldKey = childKind.getComponentName();
            Object oldValue = fromComposite.value().get(unqualify(oldKey, true));
            RuleContext context = new RuleContext(oldKey, oldValue, childKind, fromTx, toTx);

            if (childKind.getStereotype().getModel() == Archetype.CONTAINER) {
                Resource<?> oldRef = (Resource<?>) oldValue;
                Object newKey = Rule.transformKey(rules, context);
                Resource<?> newValue = toTx.create(newKey.toString());
                idmap.put(oldRef.id(), newValue.id());
                toComposite.value().set(unqualify(newKey, true), newValue);
                portAggregate(oldRef.id(), fromTx, toTx);
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
            RuleContext context = new RuleContext(oldKey, oldValue, childKind, fromTx, toTx);
            Object newKey = Rule.transformKey(rules, new RuleContext(oldKey, oldValue, childKind, fromTx, toTx));

            if (childKind.getStereotype().getModel() == Archetype.CONTAINER) {
                // determine the new component name from the resource id and the
                // key transform, create it, and port it's contents
                Resource<?> oldRes = (Resource<?>) oldValue;
                Object oldComponentName = oldRes.id().kind().getComponentName();
                RuleContext containerContext = context.copy().setKey(oldComponentName).setKind(oldRes.id().kind());
                Object newComponentName = Rule.transformKey(rules, containerContext);
                Resource<?> newValue = toTx.create(newComponentName.toString());
                idmap.put(oldRes.id(), newValue.id());
                toAggr.value().set(newKey, newValue);
                // recurse into resource
                port(oldRes.id(), fromTx, toTx);
            } else if (childKind.getStereotype() == Stereotype.REF) {
                portReference(context, toAggr.value(), false);
            } else {
                portLeaf(context, toAggr.value(), false);
            }
        }
    }

    private void portReference(RuleContext context, Aggregate<Object, Object> toParent, boolean qualified) {
        // get the old value as a resource so we can look up its id and
        // find the new id in the idmap
        Resource<?> oldRef = (Resource<?>) context.getValue();
        if (oldRef == null) { return; }
        Id<?> oldId = oldRef.id();
        Object newKey = Rule.transformKey(rules, context);
        Id<?> newId = idmap.get(oldId);
        Object newValue = context.getToTx().retrieve(newId);
        toParent.set(unqualify(newKey, qualified), newValue);
    }

    private void portLeaf(RuleContext context, Aggregate<Object, Object> newParent, boolean qualified) {
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

    public static void main(String[] args) throws ConnectorException, FileNotFoundException {
        testRealm();
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
        
        Rule role = RuleBuilder.start().forKey("Person.role").valueTransform((context) -> context.getValue()).build();
        
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

        System.out.println("-----------------------------");

        Porter p = new Porter();
        p.port(oldRepo, newRepo);

    }

}
