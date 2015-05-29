package net.objectof.actof.porter.visitor;


import java.util.Collections;

import net.objectof.actof.porter.Porter;
import net.objectof.actof.porter.PorterContext;
import net.objectof.actof.porter.PorterUtil;
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
public class ResourceUpdateVisitor extends AbstractVisitor {

    public ResourceUpdateVisitor(Porter porter, Transaction tx) {
        super(porter, tx);
    }

    protected PorterContext visitContainer(PorterContext context, Id<?> parentId) {

        Resource<Aggregate<Object, Object>> toParent = getParent(parentId);
        Object value = context.getValue();

        System.out.println("Testing Container " + context);

        if (!PorterUtil.isResourceStale(context, tx, value)) { return context; }

        System.out.println("+++ Updating Container " + context);

        Object updated = porter.updateReference(context, tx, value);
        toParent.value().set(PorterUtil.unqualify(context.getKey(), toParent), updated);
        return context;
    }

    @Override
    protected PorterContext visitLeaf(PorterContext context, Id<?> parentId) {

        Resource<Aggregate<Object, Object>> toParent = getParent(parentId);
        Object value = context.getValue();

        System.out.println("Testing Leaf " + context);

        if (!PorterUtil.isResourceStale(context, tx, value)) { return context; }

        System.out.println("+++ Updating Leaf " + context);

        Object updated = porter.updateReference(context, tx, value);
        toParent.value().set(PorterUtil.unqualify(context.getKey(), toParent), updated);
        return context;
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
