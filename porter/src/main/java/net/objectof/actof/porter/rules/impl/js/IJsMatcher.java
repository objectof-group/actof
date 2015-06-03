package net.objectof.actof.porter.rules.impl.js;


import javax.script.ScriptException;

import net.objectof.actof.porter.rules.impl.AbstractJsEvaluator;
import net.objectof.actof.porter.rules.impl.Matcher;
import net.objectof.actof.porter.visitor.IPorterContext;


public class IJsMatcher extends AbstractJsEvaluator implements Matcher {

    public IJsMatcher(String js) {
        super(js);
    }

    @SuppressWarnings("restriction")
    @Override
    public boolean test(IPorterContext t) {
        try {
            jdk.nashorn.api.scripting.ScriptObjectMirror fn = getFunction();
            Object result = fn.call(null, t);
            if (result instanceof Boolean) { return (boolean) result; }
            return false;
        }
        catch (ScriptException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

}
