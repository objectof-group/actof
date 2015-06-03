package net.objectof.actof.porter.rules.impl;


import java.util.function.BiConsumer;

import net.objectof.actof.porter.visitor.IPorterContext;


public interface Listener extends BiConsumer<IPorterContext, IPorterContext> {

}
