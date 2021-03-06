package net.objectof.actof.porter.rules.impl.js;


import java.util.function.BinaryOperator;

import javax.script.ScriptException;


public class IJsBinaryOperator<T> extends AbstractJsEvaluator implements BinaryOperator<T> {

    public IJsBinaryOperator(String js) {
        super(js);
    }

    @SuppressWarnings({ "restriction", "unchecked" })
    @Override
    public T apply(T t, T u) {
        try {
            return (T) getFunction().call(null, t, u);
        }
        catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

}
