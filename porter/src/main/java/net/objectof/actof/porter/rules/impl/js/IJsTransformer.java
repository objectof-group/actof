package net.objectof.actof.porter.rules.impl.js;


import javax.script.ScriptException;

import net.objectof.actof.porter.rules.impl.Transformer;
import net.objectof.actof.porter.visitor.IPorterContext;


public class IJsTransformer extends AbstractJsEvaluator implements Transformer {

    public IJsTransformer(String js) {
        super(js);
    }

    @SuppressWarnings({ "restriction", "unchecked" })
    @Override
    public Object apply(IPorterContext source, IPorterContext destination) {
        try {
            return getFunction().call(null, source, destination);
        }
        catch (ScriptException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}
