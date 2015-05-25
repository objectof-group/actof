package net.objectof.actof.repospy.migration.rulecomponents;


import java.util.function.Function;

import net.objectof.actof.repospy.migration.PorterContext;


public interface Transformer extends Function<PorterContext, Object> {

}
