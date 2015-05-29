package net.objectof.actof.porter.visitor;


import net.objectof.actof.porter.PorterContext;
import net.objectof.actof.porter.Walker;
import net.objectof.aggr.Aggregate;
import net.objectof.model.Id;
import net.objectof.model.Kind;
import net.objectof.model.Resource;


public interface Visitor {

    public Walker getWalker();

    public void setWalker(Walker texasRanger);

    void visit(PorterContext context, Id<?> parentId);

    Iterable<Resource<?>> getEntities(Kind<?> kind);

    Iterable<Kind<?>> getCompositeParts(Id<?> compositteId);

    Iterable<Object> getAggregateParts(Resource<Aggregate<Object, Object>> aggr);

    Kind<?> getAggregateKind(Id<?> aggrId);

}
