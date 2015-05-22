package net.objectof.actof.repospy.migration;


import java.util.List;

import net.objectof.model.Kind;


public interface Rule {

    boolean match(Object key, Object value, Kind<?> kind);

    Object transformKey(Object key, Object value, Kind<?> kind);

    Object transformValue(Object key, Object value, Kind<?> kind);

    static Object transformKey(List<Rule> rules, Object key, Object value, Kind<?> kind) {
        for (Rule rule : rules) {
            if (rule.match(key, value, kind)) {
                key = rule.transformKey(key, value, kind);
            }
        }
        return key;
    }

    static Object transformValue(List<Rule> rules, Object key, Object value, Kind<?> kind) {
        for (Rule rule : rules) {
            if (rule.match(key, value, kind)) {
                value = rule.transformValue(key, value, kind);
            }
        }
        return value;
    }

}
