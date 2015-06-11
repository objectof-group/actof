package net.objectof.actof.porter.rules.impl.js;


import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import jdk.nashorn.api.scripting.ScriptObjectMirror;


public class AbstractJsEvaluator {

    protected String js;
    protected String input;

    private static ScriptEngineManager engineManager = new ScriptEngineManager();
    private static ScriptEngine engine = engineManager.getEngineByName("nashorn");

    public AbstractJsEvaluator(String js) {
        this.js = js;
        this.input = null;
    }

    public AbstractJsEvaluator(String js, String input) {
        this.js = js;
        this.input = input;
    }

    @SuppressWarnings("restriction")
    protected ScriptObjectMirror getFunction() throws ScriptException {
        ScriptContext context = new SimpleScriptContext();
        ScriptObjectMirror fn = (ScriptObjectMirror) engine.eval(js, context);
        return fn;
    }

}
