package net.objectof.actof.porter.rules.components;


import java.util.function.Function;

import net.objectof.actof.porter.visitor.PorterContext;


public interface Transformer extends Function<PorterContext, Object> {

}
