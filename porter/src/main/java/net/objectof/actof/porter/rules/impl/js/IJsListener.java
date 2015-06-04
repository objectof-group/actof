package net.objectof.actof.porter.rules.impl.js;


import javax.script.ScriptException;

import net.objectof.actof.porter.rules.impl.Listener;
import net.objectof.actof.porter.visitor.IPorterContext;


public class IJsListener extends AbstractJsEvaluator implements Listener {

    public IJsListener(String js) {
        super(js);
    }

    @SuppressWarnings("restriction")
    @Override
    public void accept(IPorterContext t, IPorterContext u) {
        try {
            getFunction().call(null, t, u);
        }
        catch (ScriptException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
    }

}
