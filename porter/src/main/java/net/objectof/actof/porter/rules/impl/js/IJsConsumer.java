package net.objectof.actof.porter.rules.impl.js;


import java.util.function.Consumer;

import javax.script.ScriptException;

import net.objectof.actof.porter.rules.impl.AbstractJsEvaluator;


public class IJsConsumer<T> extends AbstractJsEvaluator implements Consumer<T> {

    public IJsConsumer(String js) {
        super(js);
    }

    @SuppressWarnings("restriction")
    @Override
    public void accept(T t) {
        try {
            jdk.nashorn.api.scripting.ScriptObjectMirror fn = getFunction();
            fn.call(null, t);
        }
        catch (ScriptException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
    }

}
