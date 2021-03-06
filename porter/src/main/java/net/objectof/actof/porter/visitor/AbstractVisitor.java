package net.objectof.actof.porter.visitor;


import net.objectof.actof.porter.IPorterUtil;
import net.objectof.actof.porter.Porter;
import net.objectof.actof.porter.walker.Walker;
import net.objectof.model.Id;
import net.objectof.model.Kind;
import net.objectof.model.Transaction;


public abstract class AbstractVisitor implements Visitor {

    Walker walker;
    Transaction tx;
    Porter porter;

    public AbstractVisitor(Porter porter, Transaction tx) {
        this.tx = tx;
        this.porter = porter;
    }

    public Walker getWalker() {
        return walker;
    }

    public void setWalker(Walker texasRanger) {
        this.walker = texasRanger;
    }

    @Override
    public Transaction getTx() {
        return tx;
    }

    @Override
    public void setTx(Transaction tx) {
        this.tx = tx;
    }

    public void visit(IPorterContext context, Id<?> fromParentId) {

        Object visitResult;

        if (IPorterUtil.isContainer(context.getKind())) {
            visitResult = visitContainer(context, fromParentId);
        } else {
            visitResult = visitLeaf(context, fromParentId);
        }

        // It's possible that this visit will modify the tree we're walking (eg
        // array replaced with single value), so we will try and walk whatever
        // the visit returns. When no alterations to the tree are made, this
        // method should return context.getValue() (ie the original container)
        if (visitResult == null) { return; }
        walker.walk(visitResult);
    }

    @Override
    public void visit(Object key, Object value, Kind<?> kind, Id<?> parentId) {
        visit(new IPorterContext(key, value, kind), parentId);
    }

    protected abstract Object visitContainer(IPorterContext context, Id<?> parentId);

    protected abstract Object visitLeaf(IPorterContext context, Id<?> parentId);

}
