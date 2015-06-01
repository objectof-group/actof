package net.objectof.actof.porter.rules.impl.js;


import java.util.function.Predicate;

import javax.script.ScriptException;

import net.objectof.actof.porter.rules.impl.AbstractJsEvaluator;


public class IJsPredicate<T> extends AbstractJsEvaluator implements Predicate<T> {

    public IJsPredicate(String js) {
        super(js);
    }

    @SuppressWarnings("restriction")
    @Override
    public boolean test(T t) {
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
