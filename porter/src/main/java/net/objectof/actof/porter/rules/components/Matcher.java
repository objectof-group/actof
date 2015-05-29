package net.objectof.actof.porter.rules.components;


import java.util.function.Predicate;

import net.objectof.actof.porter.visitor.IPorterContext;


public interface Matcher extends Predicate<IPorterContext> {}
