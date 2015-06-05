package net.objectof.actof.porter.ui.rule.action;


import net.objectof.actof.porter.rules.RuleBuilder;
import net.objectof.actof.porter.rules.impl.js.IJsListener;
import net.objectof.actof.porter.rules.impl.js.IJsMatcher;
import net.objectof.actof.porter.rules.impl.js.IJsTransformer;
import net.objectof.actof.porter.ui.rule.condition.Stage;


public class JsAction implements Action {

    private String code;

    public JsAction(String js) {
        this.code = js;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public void accept(Stage stage, RuleBuilder builder, String field) {
        switch (stage) {
            case AFTER:
                builder.afterTransform(new IJsListener(code, field));
                break;
            case BEFORE:
                builder.beforeTransform(new IJsListener(code, field));
                break;
            case KEY:
                builder.keyTransform(new IJsTransformer(code, field));
                break;
            case MATCH:
                builder.match(new IJsMatcher(code, field));
                break;
            case VALUE:
                builder.valueTransform(new IJsTransformer(code, field));
                break;
        }
    }
}
