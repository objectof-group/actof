package net.objectof.actof.porter.visitor;


import net.objectof.actof.porter.Porter;
import net.objectof.actof.porter.PorterContext;
import net.objectof.aggr.Aggregate;
import net.objectof.model.Resource;


public interface Visitor {

    PorterContext visitContainer(Porter porter, PorterContext context, Resource<Aggregate<Object, Object>> toParent);

    PorterContext visitLeaf(Porter porter, PorterContext context, Resource<Aggregate<Object, Object>> toParent);

}
