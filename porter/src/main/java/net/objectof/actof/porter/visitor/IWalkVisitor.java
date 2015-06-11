package net.objectof.actof.porter.visitor;


import net.objectof.actof.porter.IPorterUtil;
import net.objectof.actof.porter.Porter;
import net.objectof.actof.porter.rules.Rule;
import net.objectof.aggr.Aggregate;
import net.objectof.model.Id;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Transaction;


public class IWalkVisitor extends AbstractVisitor {

    public IWalkVisitor(Porter porter, Transaction tx) {
        super(porter, tx);
    }

    @Override
    public Iterable<Resource<?>> getEntities(Kind<?> kind) {
        return tx.enumerate(kind.getComponentName());
    }

    protected Object visitContainer(IPorterContext context, Id<?> parentId) {

        Resource<Aggregate<Object, Object>> toParent = getParent(parentId);

        IPorterContext ported = transform(context);
        if (ported.isDropped()) { return null; }

        if (toParent != null) {
            toParent.value().set(IPorterUtil.unqualify(ported.getKey(), toParent), ported.getValue());
        }
        // return the value, since we're not modifying the tree in-place
        return context.getValue();

    }

    protected Object visitLeaf(IPorterContext context, Id<?> parentId) {
        Resource<Aggregate<Object, Object>> parent = getParent(parentId);
        IPorterContext ported = transform(context);
        if (ported.isDropped()) { return null; }
        parent.value().set(IPorterUtil.unqualify(ported.getKey(), parent), ported.getValue());
        return context.getValue();
    }

    private Resource<Aggregate<Object, Object>> getParent(Id<?> parentId) {
        Resource<Aggregate<Object, Object>> parent = null;
        if (parentId != null) {
            parent = tx.retrieve(parentId);
        }
        return parent;
    }

    // @Override
    // protected Object visitContainer(IPorterContext context, Id<?> parentId) {
    // transform(context);
    // return context.getValue();
    // }
    //
    // @Override
    // protected Object visitLeaf(IPorterContext context, Id<?> parentId) {
    // transform(context);
    // return context.getValue();
    // }

    private IPorterContext transform(IPorterContext context) {

        context = context.copy();
        IPorterContext result = new IPorterContext();
        context.setTx(new ITransactionDecorator(porter, tx));
        result.setTx(new ITransactionDecorator(porter, tx));

        Rule.beforeTransform(porter.getRules(), context, context);
        if (result.isDropped()) {
            // We're using dropped here to mean 'do not recurse'
            context.setDropped(true);
            return context;
        }

        // key -- only allow modification of the key at this stage
        IPorterContext keyContext = Rule.transformKey(porter.getRules(), context.copy(), result.copy());
        result.setKey(keyContext.getKey());

        // kind -- only allow modification of the kind at this stage
        Kind<?> kind = IPorterUtil.kindFromKey(result.getTx(), keyContext.getKey().toString());
        result.setKind(kind);

        // value - not necessarily a reference, or even a resource -- only allow
        // modification of the value at this stage
        IPorterContext valueContext = Rule.transformValue(porter.getRules(), context.copy(), result.copy());
        result.setValue(valueContext.getValue());

        // after the transformation is done (not any recursion), call
        // afterTransform. Modifications made to contexts in these hooks should
        // be able to alter the real contexts, so don't copy them
        Rule.afterTransform(porter.getRules(), context, result);

        return result;
    }
}
