package net.objectof.actof.repospy.migration.rulecomponents.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.objectof.actof.repospy.migration.rulecomponents.Matcher;
import net.objectof.model.Kind;


public class KeyMatcher implements Matcher {

    List<Object> matchingKeys = new ArrayList<>();

    public KeyMatcher(Object... key) {
        this.matchingKeys.addAll(Arrays.asList(key));
    }

    @Override
    public boolean test(Object key, Object value, Kind<?> kind) {
        return matchingKeys.contains(key);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("key = ");
        if (matchingKeys.size() > 1) {
            sb.append("{");
        }

        sb.append(matchingKeys.stream().map(k -> "'" + k + "'").reduce((a, b) -> a + ", " + b).get());

        if (matchingKeys.size() > 1) {
            sb.append("}");
        }

        return sb.toString();
    }

}
