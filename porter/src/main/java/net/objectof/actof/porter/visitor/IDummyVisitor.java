package net.objectof.actof.porter.visitor;


import net.objectof.actof.porter.Porter;
import net.objectof.model.Id;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Transaction;


public class IDummyVisitor extends AbstractVisitor {

    public IDummyVisitor(Porter porter, Transaction tx) {
        super(porter, tx);
    }

    @Override
    public Iterable<Resource<?>> getEntities(Kind<?> kind) {
        return tx.enumerate(kind.getComponentName());
    }

    @Override
    protected Object visitContainer(IPorterContext context, Id<?> parentId) {
        return context.getValue();
    }

    @Override
    protected Object visitLeaf(IPorterContext context, Id<?> parentId) {
        return context.getValue();
    }

}
