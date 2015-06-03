package net.objectof.actof.porter.rules.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.objectof.actof.porter.visitor.IPorterContext;


public class IKeyMatcher implements Matcher {

    List<Object> matchingKeys = new ArrayList<>();

    public IKeyMatcher(Object... key) {
        this.matchingKeys.addAll(Arrays.asList(key));
    }

    @Override
    public boolean test(IPorterContext context) {
        return matchingKeys.contains(context.getKey());
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
