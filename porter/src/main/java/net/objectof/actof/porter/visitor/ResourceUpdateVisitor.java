package net.objectof.actof.porter.visitor;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.objectof.actof.porter.Porter;
import net.objectof.actof.porter.PorterContext;
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
public class ResourceUpdateVisitor implements Visitor {

    private Map<String, List<Resource<?>>> transients = new HashMap<>();

    @Override
    public PorterContext visitContainer(Porter porter, PorterContext context,
            Resource<Aggregate<Object, Object>> toParent) {
        return context;
    }

    @Override
    public PorterContext visitLeaf(Porter porter, PorterContext context, Resource<Aggregate<Object, Object>> toParent) {
        Object value = context.getValue();

        System.out.println("----------------------------- Testing Resource " + context);

        if (!porter.isResourceStale(context, context.getValue())) { return context; }

        System.out.println("+++++++++++++++++++++++++++++ Updating Resource " + context);

        Object updated = porter.updateReference(context, value);
        toParent.value().set(porter.unqualify(context.getKey(), toParent), updated);
        return context;
    }

    @Override
    public void onCreate(String kind, Resource<?> res) {
        // store in list of transients in new repo
        List<Resource<?>> created;
        if (!transients.containsKey(kind)) {
            transients.put(kind, new ArrayList<>());
        }
        created = transients.get(kind);
        created.add(res);
    }

    @Override
    public Iterable<Resource<?>> getEntities(Kind<?> kind, Transaction fromTx, Transaction toTx) {
        if (!transients.containsKey(kind.getComponentName())) { return Collections.emptyList(); }
        return transients.get(kind.getComponentName());
    }

    @Override
    public Iterable<Kind<?>> getCompositeParts(Id<?> from, Id<?> to) {
        return (Iterable<Kind<?>>) from.kind().getParts();
    }

    @Override
    public Iterable<Object> getAggregateParts(Resource<Aggregate<Object, Object>> from,
            Resource<Aggregate<Object, Object>> to) {
        return from.value().keySet();
    }

    @Override
    public Kind<?> getAggregateKind(Id<?> from, Id<?> to) {
        return from.kind().getParts().get(0);
    }
}
