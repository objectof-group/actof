package net.objectof.actof.repospy.migration.rulecomponents;


import net.objectof.model.Kind;


public interface Transformer extends TriFunction<Object, Object, Kind<?>, Object> {

}
