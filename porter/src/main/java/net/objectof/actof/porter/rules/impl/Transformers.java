package net.objectof.actof.porter.rules.impl;


import java.util.function.BinaryOperator;

import net.objectof.aggr.Composite;
import net.objectof.aggr.Listing;
import net.objectof.aggr.Mapping;


public class Transformers {

    public static Transformer valueToList() {
        return (src, dest) -> {
            Listing<Object> list = dest.getTx().create(dest.getKind().getComponentName());
            list.add(src.getValue());
            return list;
        };
    }

    public static Transformer listElement(BinaryOperator<Object> comparator) {
        return (src, dest) -> {
            Listing<Object> list = (Listing<Object>) src.getValue();
            if (list.size() == 0) { return null; }
            return list.stream().reduce(comparator);
        };
    }

    public static Transformer listHead() {
        return listElement((a, b) -> a);
    }

    public static Transformer listTail() {
        return listElement((a, b) -> b);
    }

    public static Transformer valueToMap(String key) {
        return (src, dest) -> {
            Mapping<Object, Object> map = dest.getTx().create(dest.getKind().getComponentName());
            map.put(key, src.getValue());
            return map;
        };
    }

    public static Transformer mapElement(String key) {
        return (src, dest) -> {
            Mapping<Object, Object> map = (Mapping<Object, Object>) src.getValue();
            if (!map.containsKey(key)) { return null; }
            return map.get(key);
        };
    }

    public static Transformer valueToComposite(String text) {
        return (src, dest) -> {
            Composite comp = dest.getTx().create(dest.getKind().getComponentName());
            comp.set(text, src.getValue());
            return comp;
        };
    }

    public static Transformer compositeElement(String text) {
        return (src, dest) -> {
            Composite comp = (Composite) src.getValue();
            return comp.get(text);
        };
    }
}
