package net.objectof.actof.porter.rules.impl;


import net.objectof.actof.porter.visitor.IPorterContext;
import net.objectof.aggr.Listing;


public class ValueToListTransformer implements Transformer {

    private String listKind;

    public ValueToListTransformer(String listKind) {
        this.listKind = listKind;
    }

    @Override
    public Object apply(IPorterContext t) {
        Listing<Object> list = t.getToTx().create(listKind);
        list.add(t.getValue());
        return list;
    }

}
