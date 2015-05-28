package net.objectof.actof.porter.visitor;


import net.objectof.actof.porter.Porter;
import net.objectof.actof.porter.PorterContext;
import net.objectof.aggr.Aggregate;
import net.objectof.model.Id;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Transaction;


public interface Visitor {

    PorterContext visitContainer(Porter porter, PorterContext context, Resource<Aggregate<Object, Object>> toParent);

    PorterContext visitLeaf(Porter porter, PorterContext context, Resource<Aggregate<Object, Object>> toParent);

    void onCreate(String kind, Resource<?> res);

    Iterable<Resource<?>> getEntities(Kind<?> kind, Transaction fromTx, Transaction toTx);

    Iterable<Kind<?>> getCompositeParts(Id<?> from, Id<?> to);

    Iterable<Object> getAggregateParts(Resource<Aggregate<Object, Object>> from, Resource<Aggregate<Object, Object>> to);

    Kind<?> getAggregateKind(Id<?> from, Id<?> to);

}
