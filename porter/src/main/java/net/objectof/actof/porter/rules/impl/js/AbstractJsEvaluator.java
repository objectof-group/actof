package net.objectof.actof.porter.rules.impl.js;


import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import jdk.nashorn.api.scripting.ScriptObjectMirror;


public class AbstractJsEvaluator {

    protected String js;

    public AbstractJsEvaluator(String js) {
        this.js = js;
    }

    protected ScriptObjectMirror getFunction() throws ScriptException {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine engine = engineManager.getEngineByName("nashorn");
        ScriptObjectMirror fn = (ScriptObjectMirror) engine.eval(js);
        return fn;
    }

}
