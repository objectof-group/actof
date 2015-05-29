package net.objectof.actof.porter.visitor;


import java.util.Collections;

import net.objectof.actof.porter.Porter;
import net.objectof.actof.porter.IPorterUtil;
import net.objectof.actof.porter.Walker;
import net.objectof.aggr.Aggregate;
import net.objectof.model.Id;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Transaction;


/**
 * Visitor for updating references from the old repo in the new
 * 
 * @author NAS
 *
 */
public class IResourceUpdateVisitor extends AbstractVisitor {

    public IResourceUpdateVisitor(Porter porter, Transaction tx) {
        super(porter, tx);
    }

    protected Object visitContainer(IPorterContext context, Id<?> parentId) {
        return doVisit(context, parentId);
    }

    @Override
    protected Object visitLeaf(IPorterContext context, Id<?> parentId) {
        return doVisit(context, parentId);
    }

    private Object doVisit(IPorterContext context, Id<?> parentId) {

        Resource<Aggregate<Object, Object>> parent = getParent(parentId);
        Object value = context.getValue();

        if (!IPorterUtil.isResourceStale(tx, value)) { return context.getValue(); }

        Object updated = porter.updateReference(context.getKind(), tx, value);
        parent.value().set(IPorterUtil.unqualify(context.getKey(), parent), updated);
        return updated;
    }

    @Override
    public Iterable<Resource<?>> getEntities(Kind<?> kind) {
        String kindName = kind.getComponentName();
        if (!porter.getTransients().containsKey(kindName)) { return Collections.emptyList(); }
        return porter.getTransients().get(kindName);
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
    public Kind<?> getAggregateKind(Id<?> aggrId) {
        return aggrId.kind().getParts().get(0);
    }

    public Walker getWalker() {
        return walker;
    }

    public void setWalker(Walker walker) {
        this.walker = walker;
    }

    private Resource<Aggregate<Object, Object>> getParent(Id<?> parentId) {
        Resource<Aggregate<Object, Object>> parent = null;
        if (parentId != null) {
            parent = tx.retrieve(parentId);
        }
        return parent;
    }

}
