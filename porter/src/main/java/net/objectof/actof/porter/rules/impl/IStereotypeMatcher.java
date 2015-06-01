package net.objectof.actof.porter.rules.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import net.objectof.actof.porter.visitor.IPorterContext;
import net.objectof.model.Stereotype;


public class IStereotypeMatcher implements Predicate<IPorterContext> {

    List<Stereotype> stereotypes = new ArrayList<>();

    public IStereotypeMatcher(Stereotype... stereotypes) {
        this.stereotypes.addAll(Arrays.asList(stereotypes));
    }

    @Override
    public boolean test(IPorterContext context) {
        return stereotypes.contains(context.getKind().getStereotype());
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
