package net.objectof.actof.porter.rules.components.impl;


import javax.script.ScriptException;

import net.objectof.actof.porter.rules.components.Transformer;
import net.objectof.actof.porter.visitor.IPorterContext;


public class IJsTransformer extends AbstractJsEvaluator implements Transformer {

    public IJsTransformer(String js) {
        super(js);
    }

    @SuppressWarnings("restriction")
    @Override
    public Object apply(IPorterContext context) {
        try {
            jdk.nashorn.api.scripting.ScriptObjectMirror fn = getFunction();
            return fn.call(null, context);
        }
        catch (ScriptException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

}
