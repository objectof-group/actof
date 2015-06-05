package net.objectof.actof.porter.ui.rule.condition;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.objectof.actof.porter.rules.impl.Transformers;
import net.objectof.actof.porter.rules.impl.js.IJsBinaryOperator;
import net.objectof.actof.porter.rules.impl.js.IJsListener;
import net.objectof.actof.porter.rules.impl.js.IJsMatcher;
import net.objectof.actof.porter.rules.impl.js.IJsTransformer;
import net.objectof.actof.porter.ui.rule.condition.Action.Input;
import net.objectof.model.Stereotype;


public class Conditions {

    private static final String JS_MATCH = "/* boolean */ function(context) {\n\t\n}";
    private static final String JS_BEFORE = "/* void */ function(sourceContext, destContext) {\n\t\n}";
    private static final String JS_AFTER = "/* void */ function(sourceContext, destContext) {\n\t\n}";
    private static final String JS_KEY = "/* object */ function(sourceContext, destContext) {\n\t\n}";
    private static final String JS_VALUE = "/* object */ function(sourceContext, destContext) {\n\t\n}";

    private static final List<Action> actions = new ArrayList<>();

    static {

        // @formatter:off

        
        //Matchers
        actions.add(new Action(Stage.MATCH, "Key", Input.FIELD, 
                (rb, text) -> rb.matchKey(text)));
        actions.add(new Action(Stage.MATCH, "Kind", Input.FIELD, 
                (rb, text) -> rb.matchKind(text)));
        actions.add(new Action(Stage.MATCH, "Stereotype", Input.FIELD, 
                (rb, text) -> rb.matchStereotype(Stereotype.valueOf(text))));
        actions.add(new Action(Stage.MATCH, "JavaScript", Input.CODE, 
                (rb, text) -> rb.match(new IJsMatcher(text)), JS_MATCH));

        
        //Before Transform Listeners
        actions.add(new Action(Stage.BEFORE, "Drop", Input.NONE, 
                (rb, text) -> rb.drop()));
        actions.add(new Action(Stage.BEFORE, "JavaScript", Input.CODE, 
                (rb, text) -> rb.beforeTransform(new IJsListener(text)), JS_BEFORE));

        
        //Key Transformers
        actions.add(new Action(Stage.KEY, "Replace", Input.FIELD, 
                (rb, text) -> rb.setKey(text)));
        actions.add(new Action(Stage.KEY, "JavaScript", Input.CODE, 
                (rb, text) -> rb.keyTransform(new IJsTransformer(text)), JS_KEY));

        
        //Value Transformers
        actions.add(new Action(Stage.VALUE, "Replace", Input.FIELD, 
                (rb, text) -> rb.setValue(text)));
        actions.add(new Action(Stage.VALUE, "JavaScript", Input.CODE, 
                (rb, text) -> rb.valueTransform(new IJsTransformer(text)), JS_VALUE));
        //List
        actions.add(new Action(Stage.VALUE, "Wrap in List", Input.NONE, 
                (rb, text) -> rb.valueTransform(Transformers.valueToList())));
        actions.add(new Action(Stage.VALUE, "List Head", Input.NONE, 
                (rb, text) -> rb.valueTransform(Transformers.listHead())));
        actions.add(new Action(Stage.VALUE, "List Tail", Input.NONE, 
                (rb, text) -> rb.valueTransform(Transformers.listTail())));
        actions.add(new Action(Stage.VALUE, "List Element", Input.CODE, 
                (rb, text) -> rb.valueTransform(Transformers.listElement(new IJsBinaryOperator<>(text))), "function (a, b) {\n\t\n}"));
        //Map
        actions.add(new Action(Stage.VALUE, "Wrap in Map", Input.FIELD, 
                (rb, text) -> rb.valueTransform(Transformers.valueToMap(text)), "", "key"));
        actions.add(new Action(Stage.VALUE, "Map Element", Input.FIELD, 
                (rb, text) -> rb.valueTransform(Transformers.mapElement(text)), "", "key"));
        //Composite
        actions.add(new Action(Stage.VALUE, "Wrap in Composite", Input.FIELD, 
                (rb, text) -> rb.valueTransform(Transformers.valueToComposite(text)), "", "field"));
        actions.add(new Action(Stage.VALUE, "Composite Element", Input.FIELD, 
                (rb, text) -> rb.valueTransform(Transformers.compositeElement(text)), "", "field"));
        
        //After Transform Listeners
        actions.add(new Action(Stage.AFTER, "JavaScript", Input.CODE, 
                (rb, text) -> rb.afterTransform(new IJsListener(text)), JS_AFTER));

        // @formatter:on

    }

    public static List<Action> forStage(Stage stage) {
        return actions.stream().filter(a -> a.stage == stage).collect(Collectors.toList());
    }
}
