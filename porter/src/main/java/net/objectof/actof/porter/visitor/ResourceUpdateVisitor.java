package net.objectof.actof.porter.visitor;


import net.objectof.actof.porter.Porter;
import net.objectof.actof.porter.PorterContext;
import net.objectof.aggr.Aggregate;
import net.objectof.model.Resource;


/**
 * Visitor for updating references from the old repo in the new
 * 
 * @author NAS
 *
 */
public class ResourceUpdateVisitor implements Visitor {

    @Override
    public PorterContext visitContainer(Porter porter, PorterContext context,
            Resource<Aggregate<Object, Object>> toParent) {
        return context;
    }

    @Override
    public PorterContext visitLeaf(Porter porter, PorterContext context, Resource<Aggregate<Object, Object>> toParent) {
        Object value = context.getValue();
        if (!porter.isResourceStale(context, context.getValue())) { return context; }

        Object updated = porter.updateReference(context, value);
        toParent.value().set(porter.unqualify(context.getKey(), toParent), updated);
        return context;
    }
}
