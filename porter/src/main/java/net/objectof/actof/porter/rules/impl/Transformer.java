package net.objectof.actof.porter.rules.impl;


import java.util.function.BiFunction;

import net.objectof.actof.porter.visitor.IPorterContext;


public interface Transformer extends BiFunction<IPorterContext, IPorterContext, Object> {

}
