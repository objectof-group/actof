package net.objectof.actof.porter.rules.components.impl;


import java.util.function.Predicate;

import javax.script.ScriptException;

import net.objectof.actof.porter.visitor.IPorterContext;


public class IJsPredicate extends AbstractJsEvaluator implements Predicate<IPorterContext> {

    public IJsPredicate(String js) {
        super(js);
    }

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
