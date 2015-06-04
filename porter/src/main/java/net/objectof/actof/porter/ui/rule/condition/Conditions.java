package net.objectof.actof.porter.ui.rule.condition;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.objectof.actof.porter.rules.impl.ValueToListTransformer;
import net.objectof.actof.porter.rules.impl.js.IJsListener;
import net.objectof.actof.porter.rules.impl.js.IJsMatcher;
import net.objectof.actof.porter.rules.impl.js.IJsTransformer;
import net.objectof.actof.porter.ui.rule.condition.Action.Input;
import net.objectof.model.Stereotype;


public class Conditions {

    private static final String JS_MATCH = "/* boolean */ function(context) {\n\t\n}";
    private static final String JS_BEFORE = "/* void */ function(context) {\n\t\n}";
    private static final String JS_AFTER = "/* void */ function(beforeContext, afterContext) {\n\t\n}";
    private static final String JS_KEY = "/* object */ function(context) {\n\t\n}";
    private static final String JS_VALUE = "/* object */ function(context) {\n\t\n}";

    private static final List<Action> actions = new ArrayList<>();

    static {

        // @formatter:off

        actions.add(new Action(Stage.MATCH, "Key", Input.FIELD, (rb, text) -> rb.matchKey(text)));
        actions.add(new Action(Stage.MATCH, "Kind", Input.FIELD, (rb, text) -> rb.matchKind(text)));
        actions.add(new Action(Stage.MATCH, "Stereotype", Input.FIELD, (rb, text) -> rb.matchStereotype(Stereotype.valueOf(text))));
        actions.add(new Action(Stage.MATCH, "JavaScript", Input.CODE, (rb, text) -> rb.match(new IJsMatcher(text)), JS_MATCH));

        actions.add(new Action(Stage.BEFORE, "Drop", Input.NONE, (rb, text) -> rb.drop()));
        actions.add(new Action(Stage.BEFORE, "JavaScript", Input.CODE, (rb, text) -> rb.beforeTransform(new IJsListener(text)), JS_BEFORE));

        actions.add(new Action(Stage.KEY, "Replace", Input.FIELD, (rb, text) -> rb.setKey(text)));
        actions.add(new Action(Stage.KEY, "JavaScript", Input.CODE, (rb, text) -> rb.keyTransform(new IJsTransformer(text)), JS_KEY));

        actions.add(new Action(Stage.VALUE, "Replace", Input.FIELD, (rb, text) -> rb.setValue(text)));
        actions.add(new Action(Stage.VALUE, "JavaScript", Input.CODE, (rb, text) -> rb.valueTransform(new IJsTransformer(text)), JS_VALUE));
        actions.add(new Action(Stage.VALUE, "Wrap in List", Input.NONE, (rb, text) -> rb.valueTransform(new ValueToListTransformer())));
        
        actions.add(new Action(Stage.AFTER, "JavaScript", Input.CODE, (rb, text) -> rb.afterTransform(new IJsListener(text)), JS_AFTER));

        // @formatter:on

    }

    public static List<Action> forStage(Stage stage) {
        return actions.stream().filter(a -> a.stage == stage).collect(Collectors.toList());
    }
}
