package net.objectof.actof.porter;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.objectof.actof.porter.rules.Rule;
import net.objectof.actof.porter.visitor.MigrationVisitor;
import net.objectof.actof.porter.visitor.Visitor;
import net.objectof.aggr.Aggregate;
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

    private Visitor visitor;

    public Porter() {}

    public Porter(Rule... rule) {
        rules.addAll(Arrays.asList(rule));
    }

    public void port(Package from, Package to) {

        idmap.clear();

        visitor = new MigrationVisitor();
        walkEntities(from, to);

    }

    private void walkEntities(Package from, Package to) {

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
                visit(context, null);
            }
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
        addRules(rules);
    }

    public void addRules(Rule... rules) {
        this.rules.addAll(Arrays.asList(rules));
    }

    public void runLater(Runnable job) {
        referenceJobs.add(job);
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

        System.out.println("Walking " + fromId);

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
            Object oldKey = childKind.getComponentName();
            Object oldValue = fromComposite.value().get(unqualify(oldKey, fromComposite));
            PorterContext context = new PorterContext(oldKey, oldValue, childKind, fromTx, toTx);
            visit(context, toComposite);
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
            Object oldValue = fromAggr.value().get(oldKey);
            PorterContext context = new PorterContext(oldKey, oldValue, childKind, fromTx, toTx);
            visit(context, toAggr);
        }
    }

    private void visit(PorterContext context, Resource<Aggregate<Object, Object>> parent) {

        System.out.println("Visiting " + context.getKind());

        if (isContainer(context.getKind())) {

            // visit the container itself
            PorterContext ported = visitor.visitContainer(this, context, parent);

            // recurse into the old container's fields/elements
            if (ported.isDropped()) { return; }
            Resource<?> res = (Resource<?>) context.getValue();
            if (res == null) { return; }
            walkContainer(res.id(), context.getFromTx(), context.getToTx());

        } else {
            visitor.visitLeaf(this, context, parent);
        }
    }

    /**
     * Accepts a PorterContext containing the source key/value/kind and returns
     * a PorterContext containing the transformed key/value/kind
     * 
     * @param context
     *            the source context
     * @return a context containing the transformed value
     */
    public PorterContext transform(PorterContext context) {

        System.out.println("Transforming " + context.getKind());

        PorterContext result = context.copy();

        // key
        PorterContext keyContext = Rule.transformKey(rules, result);
        if (keyContext.isDropped()) {
            result.setDropped(true);
            return result;
        }
        result.setKey(keyContext.getKey());

        // kind
        Kind<?> kind = kindFromKey(context.getToTx(), keyContext.getKey().toString());
        result.setKind(kind);

        // value - not necessarily a reference, or even a resource
        PorterContext valueContext = Rule.transformValue(rules, context);
        if (valueContext.isDropped()) {
            result.setDropped(true);
            return result;
        }
        Object newValue = updateReference(context, valueContext.getValue());
        result.setValue(newValue);

        // after the transformation is done (not any recursion), call onPort
        Rule.onPort(rules, context, result);

        return result;
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
    public Object updateReference(PorterContext context, Object object) {
        if (!isResourceStale(context, object)) { return object; }
        Resource<Object> res = (Resource<Object>) object;
        Id<?> oldId = res.id();
        return fetch(oldId, context.getToTx(), kindName(context.getKind()));
    }

    /**
     * Checks if the given object is a resource from the old repo
     * 
     * @param context
     * @param object
     * @return
     */
    public boolean isResourceStale(PorterContext context, Object object) {
        if (!(object instanceof Resource)) { return false; }
        Resource<Object> res = (Resource<Object>) object;
        if (res.tx().getPackage().equals(context.getToTx().getPackage())) { return false; }
        return true;
    }

    private String kindName(Kind<?> kind) {
        if (kind.getStereotype() == Stereotype.REF) {
            return kindName(kind.getParts().get(0));
        } else {
            return kind.getComponentName();
        }
    }

    private boolean isContainer(Kind<?> kind) {
        return kind.getStereotype().getModel() == Archetype.CONTAINER;
    }

    private boolean isQualified(Resource<?> parent) {
        if (parent == null) {
            return true;
        } else {
            return parent.id().kind().getStereotype() == Stereotype.COMPOSED;
        }
    }

    public Object unqualify(Object key, Resource<Aggregate<Object, Object>> parent) {
        if (!isQualified(parent)) { return key; }
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
            String componentName = kind.getComponentName();
            if (componentName.equals(key)) { return kind; }
            if (key.startsWith(componentName)) {
                Kind<?> recursed = kindFromKey(kind.getParts(), key);
                if (recursed != null) { return recursed; }
            }
        }
        return null;
    }

    private Resource<Object> fetch(Id<?> fromId, Transaction toTx, String kind) {
        if (!idmap.containsKey(fromId)) {
            create(fromId, toTx, kind);
        }
        Id<?> toId = idmap.get(fromId);
        return toTx.retrieve(toId);
    }

    private Resource<Object> create(Id<?> fromId, Transaction toTx, String kind) {
        System.out.println("Creating " + kind);
        Resource<Object> newValue = toTx.create(kind);
        idmap.put(fromId, newValue.id());
        return newValue;
    }

}
