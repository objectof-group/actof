package net.objectof.actof.porter.rules.impl;


import net.objectof.actof.porter.visitor.IPorterContext;
import net.objectof.aggr.Listing;


public class ValueToListTransformer implements Transformer {

    private String listKind;

    public ValueToListTransformer(String listKind) {
        this.listKind = listKind;
    }

    @Override
    public Object apply(IPorterContext source, IPorterContext destination) {
        Listing<Object> list = source.getToTx().create(listKind);
        list.add(source.getValue());
        return list;
    }

}
