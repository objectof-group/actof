package net.objectof.actof.porter.ui.rule.condition;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.objectof.actof.porter.rules.impl.js.IJsBiConsumer;
import net.objectof.actof.porter.rules.impl.js.IJsConsumer;
import net.objectof.actof.porter.rules.impl.js.IJsPredicate;
import net.objectof.actof.porter.rules.impl.js.IJsTransformer;
import net.objectof.actof.porter.ui.rule.condition.Action.Input;
import net.objectof.model.Stereotype;


public class Conditions {

    public static Map<String, List<Action>> conditions = new LinkedHashMap<>();
    static {
        conditions.put("Match", new ArrayList<Action>());
        conditions.put("Before", new ArrayList<Action>());
        conditions.put("Key Transform", new ArrayList<Action>());
        conditions.put("Value Transform", new ArrayList<Action>());
        conditions.put("After", new ArrayList<Action>());

        List<Action> before = conditions.get("Before");
        List<Action> match = conditions.get("Match");
        List<Action> keyTransform = conditions.get("Key Transform");
        List<Action> valueTransform = conditions.get("Value Transform");
        List<Action> after = conditions.get("After");

        match.add(new Action("Key", Input.SMALL, (rb, text) -> rb.matchKey(text)));
        match.add(new Action("Kind", Input.SMALL, (rb, text) -> rb.matchKind(text)));
        match.add(new Action("Stereotype", Input.SMALL, (rb, text) -> rb.matchStereotype(Stereotype.valueOf(text))));
        match.add(new Action("JavaScript", Input.LARGE, (rb, text) -> rb.match(new IJsPredicate<>(text))));

        before.add(new Action("Drop", Input.NONE, (rb, text) -> rb.drop()));
        before.add(new Action("JavaScript", Input.LARGE, (rb, text) -> rb.beforeTransform(new IJsConsumer<>(text))));

        keyTransform.add(new Action("Replace", Input.SMALL, (rb, text) -> rb.setKey(text)));
        keyTransform.add(new Action("JavaScript", Input.LARGE, (rb, text) -> rb
                .keyTransform(new IJsTransformer<>(text))));

        valueTransform.add(new Action("Replace", Input.SMALL, (rb, text) -> rb.setValue(text)));
        valueTransform.add(new Action("JavaScript", Input.LARGE, (rb, text) -> rb.valueTransform(new IJsTransformer<>(
                text))));

        after.add(new Action("JavaScript", Input.LARGE, (rb, text) -> rb.afterTransform(new IJsBiConsumer<>(text))));

    }
}
