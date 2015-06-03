package net.objectof.actof.porter.rules.impl;


import java.util.function.Function;

import net.objectof.actof.porter.visitor.IPorterContext;


public interface Transformer extends Function<IPorterContext, Object> {

}
