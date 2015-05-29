package net.objectof.actof.porter.visitor;


import net.objectof.actof.porter.Porter;
import net.objectof.actof.porter.IPorterUtil;
import net.objectof.actof.porter.Walker;
import net.objectof.model.Id;
import net.objectof.model.Kind;
import net.objectof.model.Transaction;


public abstract class AbstractVisitor implements Visitor {

    Porter porter;
    Walker walker;
    Transaction tx;

    public AbstractVisitor(Porter porter, Transaction tx) {
        this.porter = porter;
        this.tx = tx;
    }

    public Walker getWalker() {
        return walker;
    }

    public void setWalker(Walker texasRanger) {
        this.walker = texasRanger;
    }

    public void visit(IPorterContext context, Id<?> fromParentId) {

        System.out.println("Visiting " + context.getKind());

        if (IPorterUtil.isContainer(context.getKind())) {

            // visit the container itself. It's possible that this visit will
            // modify the tree we're walking (eg array replaced with single
            // value), so we will try and walk whatever the visitContainer
            // method returns. When no alterations to the tree are made, this
            // method should return context.getValue() (ie the original
            // container)
            Object visitResult = visitContainer(context, fromParentId);
            if (visitResult == null) { return; }
            // try to walk whatever the visit produced
            walker.walk(visitResult);

        } else {
            // visit this leaf. It's possible that this visit will modify the
            // tree we're walking (eg leaf replaced with array), so we will try
            // and walk whatever the visitContainer method returns. When no
            // alterations to the tree are made, this method should return
            // context.getValue() (ie the original container)
            Object visitResult = visitLeaf(context, fromParentId);
            if (visitResult == null) { return; }
            // try to walk whatever the visit produced
            walker.walk(visitResult);
        }
    }

    @Override
    public void visit(Object key, Object value, Kind<?> kind, Id<?> parentId) {
        visit(new IPorterContext(key, value, kind), parentId);
    }

    protected abstract Object visitContainer(IPorterContext context, Id<?> parentId);

    protected abstract Object visitLeaf(IPorterContext context, Id<?> parentId);

}
