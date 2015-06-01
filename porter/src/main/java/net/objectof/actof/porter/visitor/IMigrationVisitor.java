package net.objectof.actof.porter.visitor;


import net.objectof.actof.porter.IPorterUtil;
import net.objectof.actof.porter.ITransactionDecorator;
import net.objectof.actof.porter.Porter;
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
public class IMigrationVisitor extends AbstractVisitor {

    Transaction targetTx;

    public IMigrationVisitor(Porter porter, Transaction fromTx, Transaction targetTx) {
        super(porter, fromTx);
        this.targetTx = targetTx;
    }

    protected Object visitContainer(IPorterContext context, Id<?> parentId) {

        Resource<Aggregate<Object, Object>> toParent = getToParent(parentId);

        IPorterContext ported = transform(context);
        if (ported.isDropped()) { return null; }

        if (ported.getKind().getStereotype() == Stereotype.REF) {
            // there's a chance that the user passed us a reference to something
            // in the old repo.
            porter.runLater(() -> {
                if (toParent == null) { return; }
                toParent.value().set(IPorterUtil.unqualify(ported.getKey(), toParent), ported.getValue());
            });

            // return value is the container to walk. With a reference, we don't
            // need to walk anything.
            return null;
        } else {
            if (toParent != null) {
                toParent.value().set(IPorterUtil.unqualify(ported.getKey(), toParent), ported.getValue());
            }
            // return value is the container to walk. Just return the
            // container we're porting
            return context.getValue();

        }

    }

    protected Object visitLeaf(IPorterContext context, Id<?> parentId) {
        Resource<Aggregate<Object, Object>> toParent = getToParent(parentId);
        IPorterContext ported = transform(context);
        if (ported.isDropped()) { return null; }
        toParent.value().set(IPorterUtil.unqualify(ported.getKey(), toParent), ported.getValue());
        return context.getValue();
    }

    @Override
    public Iterable<Resource<?>> getEntities(Kind<?> kind) {
        return tx.enumerate(kind.getComponentName());
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
    private IPorterContext transform(IPorterContext originalContext) {

        IPorterContext context = originalContext.copy();

        context.setFromTx(new ITransactionDecorator(porter, tx));
        context.setToTx(new ITransactionDecorator(porter, targetTx));

        // before the transformation starts, call beforeTransform. Modifications
        // made to contexts in these hooks should be able to alter the real
        // contexts, so don't pass copies
        Rule.beforeTransform(porter.getRules(), context);
        if (context.isDropped()) { return context; }

        // result is based on a copy of context *after* called beforeTransform
        IPorterContext result = context.copy();

        // key -- only allow modification of the key at this stage
        IPorterContext keyContext = Rule.transformKey(porter.getRules(), context.copy());
        System.out.println(keyContext);
        if (keyContext.isDropped()) {
            result.setDropped(true);
            return result;
        }
        result.setKey(keyContext.getKey());

        // kind -- only allow modification of the kind at this stage
        Kind<?> kind = IPorterUtil.kindFromKey(context.getToTx(), keyContext.getKey().toString());
        result.setKind(kind);

        // value - not necessarily a reference, or even a resource -- only allow
        // modification of the value at this stage
        IPorterContext valueContext = Rule.transformValue(porter.getRules(), context.copy());
        if (valueContext.isDropped()) {
            result.setDropped(true);
            return result;
        }
        Object newValue = porter.updateReference(context.getKind(), targetTx, valueContext.getValue());
        result.setValue(newValue);

        // after the transformation is done (not any recursion), call
        // afterTransform. Modifications made to contexts in these hooks should
        // be able to alter the real contexts, so don't copy them
        Rule.afterTransform(porter.getRules(), context, result);

        return result;
    }
}
