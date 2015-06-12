package net.objectof.actof.porter.rules.impl.js;


import javax.script.ScriptException;

import net.objectof.actof.porter.rules.impl.Transformer;
import net.objectof.actof.porter.visitor.IPorterContext;


public class IJsTransformer extends AbstractJsEvaluator implements Transformer {

    public IJsTransformer(String js) {
        super(js);
    }

    public IJsTransformer(String js, String input) {
        super(js, input);
    }

    @SuppressWarnings({ "restriction" })
    @Override
    public Object apply(IPorterContext source, IPorterContext destination) {
        try {
            if (input == null) {
                return getFunction().call(null, source, destination);
            } else {
                return getFunction().call(null, source, destination, input);
            }
        }
        catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

}
