package net.objectof.actof.porter.visitor;


import net.objectof.actof.porter.Porter;
import net.objectof.actof.porter.PorterUtil;
import net.objectof.actof.porter.TransactionDecorator;
import net.objectof.actof.porter.rules.Rule;
import net.objectof.aggr.Aggregate;
import net.objectof.model.Id;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;
import net.objectof.model.Transaction;


/**
 * Visitor for migrating old repo tree to new repo tree
 * 
 * @author NAS
 *
 */
public class MigrationVisitor extends AbstractVisitor {

    Transaction targetTx;

    public MigrationVisitor(Porter porter, Transaction fromTx, Transaction targetTx) {
        super(porter, fromTx);
        this.targetTx = targetTx;
    }

    protected PorterContext visitContainer(PorterContext context, Id<?> parentId) {

        Resource<Aggregate<Object, Object>> toParent = getToParent(parentId);

        PorterContext ported = transform(context);
        if (ported.isDropped()) { return ported; }

        if (ported.getKind().getStereotype() == Stereotype.REF) {
            // there's a chance that the user passed us a reference to something
            // in the old repo.
            porter.runLater(() -> {
                if (toParent == null) { return; }
                toParent.value().set(PorterUtil.unqualify(ported.getKey(), toParent), ported.getValue());
            });
        } else {
            if (toParent == null) { return ported; }
            toParent.value().set(PorterUtil.unqualify(ported.getKey(), toParent), ported.getValue());
        }

        return ported;
    }

    protected PorterContext visitLeaf(PorterContext context, Id<?> parentId) {
        Resource<Aggregate<Object, Object>> toParent = getToParent(parentId);
        PorterContext ported = transform(context);
        if (ported.isDropped()) { return ported; }
        toParent.value().set(PorterUtil.unqualify(ported.getKey(), toParent), ported.getValue());
        return ported;
    }

    @Override
    public Iterable<Resource<?>> getEntities(Kind<?> kind) {
        return tx.enumerate(kind.getComponentName());
    }

    @Override
    public Iterable<Kind<?>> getCompositeParts(Id<?> compositeId) {
        return (Iterable<Kind<?>>) compositeId.kind().getParts();
    }

    @Override
    public Iterable<Object> getAggregateParts(Resource<Aggregate<Object, Object>> aggr) {
        return aggr.value().keySet();
    }

    @Override
    public Kind<?> getAggregateKind(Id<?> aggr) {
        return aggr.kind().getParts().get(0);
    }

    private Resource<Aggregate<Object, Object>> getToParent(Id<?> fromParentId) {
        Id<?> toParentId = porter.getIdmap().get(fromParentId);
        Resource<Aggregate<Object, Object>> parent = null;
        if (toParentId != null) {
            parent = targetTx.retrieve(toParentId);
        }
        return parent;
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

        context.setFromTx(new TransactionDecorator(porter, tx));
        context.setToTx(new TransactionDecorator(porter, targetTx));

        // System.out.println("Transforming " + context.getKind());

        PorterContext result = context.copy();

        // key
        PorterContext keyContext = Rule.transformKey(porter.getRules(), result);
        if (keyContext.isDropped()) {
            result.setDropped(true);
            return result;
        }
        result.setKey(keyContext.getKey());

        // kind
        Kind<?> kind = PorterUtil.kindFromKey(context.getToTx(), keyContext.getKey().toString());
        result.setKind(kind);

        // value - not necessarily a reference, or even a resource
        PorterContext valueContext = Rule.transformValue(porter.getRules(), context);
        if (valueContext.isDropped()) {
            result.setDropped(true);
            return result;
        }
        Object newValue = porter.updateReference(context.getKind(), targetTx, valueContext.getValue());
        result.setValue(newValue);

        // after the transformation is done (not any recursion), call onPort
        Rule.onPort(porter.getRules(), context, result);

        return result;
    }

}
