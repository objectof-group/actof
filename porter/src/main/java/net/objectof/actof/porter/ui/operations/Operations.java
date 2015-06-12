package net.objectof.actof.porter.ui.operations;


import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.objectof.actof.porter.rules.impl.Listeners;
import net.objectof.actof.porter.rules.impl.Matchers;
import net.objectof.actof.porter.rules.impl.Transformers;
import net.objectof.actof.porter.rules.impl.js.IJsBinaryOperator;
import net.objectof.actof.porter.rules.impl.js.IJsListener;
import net.objectof.actof.porter.rules.impl.js.IJsMatcher;
import net.objectof.actof.porter.rules.impl.js.IJsTransformer;
import net.objectof.actof.porter.ui.action.JavaAction;
import net.objectof.actof.porter.ui.condition.Condition;
import net.objectof.actof.porter.ui.condition.Condition.Input;
import net.objectof.actof.porter.ui.condition.Condition.Stage;


public class Operations {

    private static final String JS_MATCH = "/* boolean */ function(context) {\n\t\n}";
    private static final String JS_BEFORE = "/* void */ function(sourceContext, destContext) {\n\t\n}";
    private static final String JS_AFTER = "/* void */ function(sourceContext, destContext) {\n\t\n}";
    private static final String JS_KEY = "/* object */ function(sourceContext, destContext) {\n\t\n}";
    private static final String JS_VALUE = "/* object */ function(sourceContext, destContext) {\n\t\n}";

    // private static final ObservableList<Condition> actions =
    // FXCollections.observableArrayList();

    private static final ObservableList<Operation> operations = FXCollections
            .observableArrayList(op -> new Observable[] { op.titleProperty() });

    static {

        // @formatter:off

        
        //**********************
        // Matchers
        //**********************
        operations.add(new Operation(
                new Condition(Stage.MATCH, "Key", Input.FIELD),
                new JavaAction().setMatcherAct(text -> Matchers.matchKey(text))));
        
        operations.add(new Operation(
                new Condition(Stage.MATCH, "Kind", Input.FIELD),
                new JavaAction().setMatcherAct(text -> Matchers.matchKind(text))));
        
        operations.add(new Operation(
                new Condition(Stage.MATCH, "Stereotype", Input.FIELD),
                new JavaAction().setMatcherAct(text -> Matchers.matchStereotype(text))));
        
        operations.add(new Operation(
                new Condition(Stage.MATCH, "JavaScript", Input.FIELD, JS_MATCH),
                new JavaAction().setMatcherAct(text -> new IJsMatcher(text))));


        //**********************
        //Before Transform Listeners
        //**********************
        operations.add(new Operation(
                new Condition(Stage.BEFORE, "Drop", Input.NONE),
                new JavaAction().setBeforeAct(text -> Listeners.drop())));
        
        operations.add(new Operation(
                new Condition(Stage.BEFORE, "JavaScript", Input.CODE, JS_BEFORE),
                new JavaAction().setBeforeAct(text -> new IJsListener(text))));
        
        operations.add(new Operation(
                new Condition(Stage.BEFORE, "Echo", Input.FIELD),
                new JavaAction().setBeforeAct(text -> Listeners.echo(text))));

        operations.add(new Operation(
                new Condition(Stage.BEFORE, "Print Key", Input.NONE),
                new JavaAction().setBeforeAct(text -> Listeners.printKey())));

        operations.add(new Operation(
                new Condition(Stage.BEFORE, "Print Value", Input.NONE),
                new JavaAction().setBeforeAct(text -> Listeners.printValue())));

        operations.add(new Operation(
                new Condition(Stage.BEFORE, "Print Kind", Input.NONE),
                new JavaAction().setBeforeAct(text -> Listeners.printKind())));
        
        operations.add(new Operation(
                new Condition(Stage.BEFORE, "Print", Input.CODE, "/* string */ function (sourceContext, destContext) {\n\t\n}"),
                new JavaAction().setBeforeAct(text -> Listeners.print(new IJsTransformer(text)))));
        
        
        //**********************
        //Key Transformers
        //**********************
        operations.add(new Operation(
            new Condition(Stage.KEY, "Replace", Input.FIELD),
            new JavaAction().setKeyAct(text -> Transformers.replace(text))));
        
        operations.add(new Operation(
            new Condition(Stage.KEY, "JavaScript", Input.CODE, JS_KEY),
            new JavaAction().setKeyAct(text -> new IJsTransformer(text))));
        
        
        //**********************
        //Value Transformers
        //**********************
        operations.add(new Operation(
            new Condition(Stage.VALUE, "Replace", Input.FIELD), 
            new JavaAction().setValueAct(text -> Transformers.replace(text))));
        
        operations.add(new Operation(
            new Condition(Stage.VALUE, "JavaScript", Input.CODE, JS_VALUE),
            new JavaAction().setValueAct(text -> new IJsTransformer(text))));
        
        //List
        operations.add(new Operation(
            new Condition(Stage.VALUE, "Wrap in List", Input.NONE), 
            new JavaAction().setValueAct(text -> Transformers.valueToList())));
        
        operations.add(new Operation(
            new Condition(Stage.VALUE, "List Head", Input.NONE), 
            new JavaAction().setValueAct(text -> Transformers.listHead())));
        
        operations.add(new Operation(
            new Condition(Stage.VALUE, "List Tail", Input.NONE), 
            new JavaAction().setValueAct(text -> Transformers.listTail())));
        
        operations.add(new Operation(
            new Condition(Stage.VALUE, "List Element", Input.CODE, "", "function (a, b) {\n\t\n}"), 
            new JavaAction().setValueAct(text -> Transformers.listElement(new IJsBinaryOperator<>(text)))));
            
            
        //Map
        operations.add(new Operation(
            new Condition(Stage.VALUE, "Wrap in Map", Input.FIELD, "", "key"), 
            new JavaAction().setValueAct(text -> Transformers.valueToMap(text))));
        
        operations.add(new Operation(
            new Condition(Stage.VALUE, "Map Element", Input.FIELD, "", "key"), 
            new JavaAction().setValueAct(text -> Transformers.mapElement(text))));
        
        //Composite
        operations.add(new Operation(
            new Condition(Stage.VALUE, "Wrap in Composite", Input.FIELD, "", "field"), 
            new JavaAction().setValueAct(text -> Transformers.valueToComposite(text))));
        
        operations.add(new Operation(
            new Condition(Stage.VALUE, "Composite Element", Input.FIELD, "", "field"), 
            new JavaAction().setValueAct(text -> Transformers.compositeElement(text))));
        
        
        //**********************
        //After Transform Listeners
        //**********************
        operations.add(new Operation(
            new Condition(Stage.AFTER, "JavaScript", Input.CODE, JS_AFTER), 
            new JavaAction().setAfterAct(text -> new IJsListener(text)))); 
        
        // @formatter:on

        List<Operation> customOps = JsOperations.load();
        for (Operation op : customOps) {
            operations.add(op);
        }

    }

    public static ObservableList<Operation> getOperations() {
        return operations;
    }

    public static List<Condition> conditionsFor(Stage stage) {
        return operations.stream().map(op -> op.getCondition()).filter(a -> a.getStage() == stage)
                .sorted((a, b) -> a.getName().compareTo(b.getName())).collect(Collectors.toList());
    }

    public static Operation forCondition(Condition condition) {
        return operations.stream().filter(op -> op.getCondition().equals(condition)).findFirst().orElse(null);
    }
}
