package net.objectof.actof.porter.rules.impl.js;


import javax.script.ScriptException;

import net.objectof.actof.porter.rules.impl.Matcher;
import net.objectof.actof.porter.visitor.IPorterContext;


public class IJsMatcher extends AbstractJsEvaluator implements Matcher {

    public IJsMatcher(String js) {
        super(js);
    }

    public IJsMatcher(String js, String input) {
        super(js, input);
    }

    @SuppressWarnings("restriction")
    @Override
    public boolean test(IPorterContext t) {
        try {
            jdk.nashorn.api.scripting.ScriptObjectMirror fn = getFunction();
            Object result = null;
            if (input == null) {
                result = fn.call(null, t);
            } else {
                result = fn.call(null, t, input);
            }
            if (result instanceof Boolean) { return (boolean) result; }
            return false;
        }
        catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

}
