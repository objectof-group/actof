package net.objectof.actof.porter.rules.components.impl;


import java.util.function.BiConsumer;

import javax.script.ScriptException;

import net.objectof.actof.porter.visitor.IPorterContext;


public class IJsPortingListener extends AbstractJsEvaluator implements BiConsumer<IPorterContext, IPorterContext> {

    public IJsPortingListener(String js) {
        super(js);
    }

    @SuppressWarnings("restriction")
    @Override
    public void accept(IPorterContext t, IPorterContext u) {
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
