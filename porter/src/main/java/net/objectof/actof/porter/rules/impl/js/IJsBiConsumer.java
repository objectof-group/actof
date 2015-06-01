package net.objectof.actof.porter.rules.impl.js;


import java.util.function.BiConsumer;

import javax.script.ScriptException;

import net.objectof.actof.porter.rules.impl.AbstractJsEvaluator;


public class IJsBiConsumer<T, U> extends AbstractJsEvaluator implements BiConsumer<T, U> {

    public IJsBiConsumer(String js) {
        super(js);
    }

    @SuppressWarnings("restriction")
    @Override
    public void accept(T t, U u) {
        try {
            jdk.nashorn.api.scripting.ScriptObjectMirror fn = getFunction();
            fn.call(null, t, u);
        }
        catch (ScriptException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
    }

}
