package net.objectof.actof.porter.rules.impl.js;


import java.util.function.Function;

import javax.script.ScriptException;

import net.objectof.actof.porter.rules.impl.AbstractJsEvaluator;


public class IJsTransformer<T, U> extends AbstractJsEvaluator implements Function<T, U> {

    public IJsTransformer(String js) {
        super(js);
    }

    @SuppressWarnings({ "restriction", "unchecked" })
    @Override
    public U apply(T context) {
        try {
            jdk.nashorn.api.scripting.ScriptObjectMirror fn = getFunction();
            return (U) fn.call(null, context);
        }
        catch (ScriptException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}
