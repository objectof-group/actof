package net.objectof.actof.porter.rules.impl.js;


import javax.script.ScriptException;

import net.objectof.actof.porter.rules.impl.Listener;
import net.objectof.actof.porter.visitor.IPorterContext;


public class IJsListener extends AbstractJsEvaluator implements Listener {

    public IJsListener(String js) {
        super(js);
    }

    public IJsListener(String js, String input) {
        super(js, input);
    }

    @SuppressWarnings("restriction")
    @Override
    public void accept(IPorterContext t, IPorterContext u) {
        try {
            if (input == null) {
                getFunction().call(null, t, u);
            } else {
                getFunction().call(null, t, u, input);
            }
        }
        catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

}
