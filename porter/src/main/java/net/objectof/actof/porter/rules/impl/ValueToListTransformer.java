package net.objectof.actof.porter.rules.impl;


import net.objectof.actof.porter.visitor.IPorterContext;
import net.objectof.aggr.Listing;


public class ValueToListTransformer implements Transformer {

    @Override
    public Object apply(IPorterContext source, IPorterContext destination) {
        Listing<Object> list = source.getToTx().create(destination.getKind().getComponentName());
        list.add(source.getValue());
        return list;
    }

}
