package net.objectof.actof.porter.visitor;


import net.objectof.actof.porter.Porter;
import net.objectof.actof.porter.PorterContext;
import net.objectof.actof.porter.PorterUtil;
import net.objectof.actof.porter.Walker;
import net.objectof.model.Id;
import net.objectof.model.Resource;
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

    public void visit(PorterContext context, Id<?> fromParentId) {

        System.out.println("Visiting " + context.getKind());

        if (PorterUtil.isContainer(context.getKind())) {

            // visit the container itself
            PorterContext ported = visitContainer(context, fromParentId);

            // recurse into the old container's fields/elements
            if (ported.isDropped()) {
                System.out.println("dropped");
                return;
            }
            Resource<?> res = (Resource<?>) context.getValue();
            if (res == null) {
                System.out.println("res is null");
                return;
            }

            walker.walkContainer(res.id());

        } else {
            visitLeaf(context, fromParentId);
        }
    }

    protected abstract PorterContext visitContainer(PorterContext context, Id<?> parentId);

    protected abstract PorterContext visitLeaf(PorterContext context, Id<?> parentId);

}
