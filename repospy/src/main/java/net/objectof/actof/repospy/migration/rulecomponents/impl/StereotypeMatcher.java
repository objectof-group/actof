package net.objectof.actof.repospy.migration.rulecomponents.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.objectof.actof.repospy.migration.rulecomponents.Matcher;
import net.objectof.model.Kind;
import net.objectof.model.Stereotype;


public class StereotypeMatcher implements Matcher {

    List<Stereotype> stereotypes = new ArrayList<>();

    public StereotypeMatcher(Stereotype... stereotypes) {
        this.stereotypes.addAll(Arrays.asList(stereotypes));
    }

    @Override
    public boolean test(Object t, Object u, Kind<?> kind) {
        return stereotypes.contains(kind.getStereotype());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Stereotype = ");
        if (stereotypes.size() > 1) {
            sb.append("{");
        }

        sb.append(stereotypes.stream().map(Stereotype::toString).reduce((a, b) -> a + ", " + b).get());

        if (stereotypes.size() > 1) {
            sb.append("}");
        }

        return sb.toString();
    }
}
