package net.objectof.actof.porter.ui.rule.condition;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.objectof.actof.porter.rules.impl.Listeners;
import net.objectof.actof.porter.rules.impl.Matchers;
import net.objectof.actof.porter.rules.impl.Transformers;
import net.objectof.actof.porter.rules.impl.js.IJsBinaryOperator;
import net.objectof.actof.porter.rules.impl.js.IJsListener;
import net.objectof.actof.porter.rules.impl.js.IJsMatcher;
import net.objectof.actof.porter.rules.impl.js.IJsTransformer;
import net.objectof.actof.porter.ui.rule.action.Action;
import net.objectof.actof.porter.ui.rule.action.JavaAction;
import net.objectof.actof.porter.ui.rule.condition.Condition.Input;
import net.objectof.actof.porter.ui.rule.operation.JsOperation;


public class Conditions {

    private static final String JS_MATCH = "/* boolean */ function(context) {\n\t\n}";
    private static final String JS_BEFORE = "/* void */ function(sourceContext, destContext) {\n\t\n}";
    private static final String JS_AFTER = "/* void */ function(sourceContext, destContext) {\n\t\n}";
    private static final String JS_KEY = "/* object */ function(sourceContext, destContext) {\n\t\n}";
    private static final String JS_VALUE = "/* object */ function(sourceContext, destContext) {\n\t\n}";

    // private static final ObservableList<Condition> actions =
    // FXCollections.observableArrayList();

    private static final Map<Condition, Action> conditions = new LinkedHashMap<>();

    static {

        // @formatter:off

        
        //**********************
        // Matchers
        //**********************
        conditions.put(
                new Condition(Stage.MATCH, "Key", Input.FIELD),
                new JavaAction().setMatcherAct(text -> Matchers.matchKey(text)));
        
        conditions.put(
                new Condition(Stage.MATCH, "Kind", Input.FIELD),
                new JavaAction().setMatcherAct(text -> Matchers.matchKind(text)));
        
        conditions.put(
                new Condition(Stage.MATCH, "Stereotype", Input.FIELD),
                new JavaAction().setMatcherAct(text -> Matchers.matchStereotype(text)));
        
        conditions.put(
                new Condition(Stage.MATCH, "JavaScript", Input.FIELD, JS_MATCH),
                new JavaAction().setMatcherAct(text -> new IJsMatcher(text)));


        //**********************
        //Before Transform Listeners
        //**********************
        conditions.put(
                new Condition(Stage.BEFORE, "Drop", Input.NONE),
                new JavaAction().setBeforeAct(text -> Listeners.drop()));
        
        conditions.put(
                new Condition(Stage.BEFORE, "JavaScript", Input.CODE, JS_BEFORE),
                new JavaAction().setBeforeAct(text -> new IJsListener(text)));
        
        conditions.put(
                new Condition(Stage.BEFORE, "Echo", Input.FIELD),
                new JavaAction().setBeforeAct(text -> Listeners.echo(text)));

        conditions.put(
                new Condition(Stage.BEFORE, "Print Key", Input.NONE),
                new JavaAction().setBeforeAct(text -> Listeners.printKey()));

        conditions.put(
                new Condition(Stage.BEFORE, "Print Value", Input.NONE),
                new JavaAction().setBeforeAct(text -> Listeners.printValue()));

        conditions.put(
                new Condition(Stage.BEFORE, "Print Kind", Input.NONE),
                new JavaAction().setBeforeAct(text -> Listeners.printKind()));
        
        conditions.put(
                new Condition(Stage.BEFORE, "Print", Input.CODE),
                new JavaAction().setBeforeAct(text -> Listeners.print(new IJsTransformer(text))));
        
        
        //**********************
        //Key Transformers
        //**********************
        conditions.put(
            new Condition(Stage.KEY, "Replace", Input.FIELD),
            new JavaAction().setKeyAct(text -> Transformers.replace(text)));
        
        conditions.put(
            new Condition(Stage.KEY, "JavaScript", Input.CODE, JS_KEY),
            new JavaAction().setKeyAct(text -> new IJsTransformer(text)));
        
        
        //**********************
        //Value Transformers
        //**********************
        conditions.put(
            new Condition(Stage.VALUE, "Replace", Input.FIELD), 
            new JavaAction().setValueAct(text -> Transformers.replace(text)));
        
        conditions.put(
            new Condition(Stage.VALUE, "JavaScript", Input.CODE, JS_VALUE),
            new JavaAction().setValueAct(text -> new IJsTransformer(text)));
        
        //List
        conditions.put(
            new Condition(Stage.VALUE, "Wrap in List", Input.NONE), 
            new JavaAction().setValueAct(text -> Transformers.valueToList()));
        
        conditions.put(
            new Condition(Stage.VALUE, "List Head", Input.NONE), 
            new JavaAction().setValueAct(text -> Transformers.listHead()));
        
        conditions.put(
            new Condition(Stage.VALUE, "List Tail", Input.NONE), 
            new JavaAction().setValueAct(text -> Transformers.listTail()));
        
        conditions.put(
            new Condition(Stage.VALUE, "List Element", Input.CODE, "", "function (a, b) {\n\t\n}"), 
            new JavaAction().setValueAct(text -> Transformers.listElement(new IJsBinaryOperator<>(text))));
            
            
        //Map
        conditions.put(
            new Condition(Stage.VALUE, "Wrap in Map", Input.FIELD, "", "key"), 
            new JavaAction().setValueAct(text -> Transformers.valueToMap(text)));
        
        conditions.put(
            new Condition(Stage.VALUE, "Map Element", Input.FIELD, "", "key"), 
            new JavaAction().setValueAct(text -> Transformers.mapElement(text)));
        
        //Composite
        conditions.put(
            new Condition(Stage.VALUE, "Wrap in Composite", Input.FIELD, "", "field"), 
            new JavaAction().setValueAct(text -> Transformers.valueToComposite(text)));
        
        conditions.put(
            new Condition(Stage.VALUE, "Composite Element", Input.FIELD, "", "field"), 
            new JavaAction().setValueAct(text -> Transformers.compositeElement(text)));
        
        
        //**********************
        //After Transform Listeners
        //**********************
        conditions.put(
            new Condition(Stage.AFTER, "JavaScript", Input.CODE, JS_AFTER), 
            new JavaAction().setAfterAct(text -> new IJsListener(text))); 
        
        // @formatter:on

        List<JsOperation> customOps = JsOperation.load();
        for (JsOperation op : customOps) {
            conditions.put(op.condition, op.action);
        }

    }

    public static List<Condition> forStage(Stage stage) {
        return conditions.keySet().stream().filter(a -> a.getStage() == stage)
                .sorted((a, b) -> a.getName().compareTo(b.getName())).collect(Collectors.toList());
    }

    public static Action forCondition(Condition condition) {
        return conditions.get(condition);
    }
}
