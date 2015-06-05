package net.objectof.actof.porter.ui.rule.condition;


public class Condition {

    public enum Input {
        NONE, FIELD, CODE;
    }

    private Stage stage;
    private String name;
    private Input input;
    private String defaultText;
    private String hint;

    public Condition(Stage stage, String name, Input input) {
        this(stage, name, input, "");
    }

    public Condition(Stage stage, String name, Input input, String defaultText) {
        this(stage, name, input, defaultText, "");
    }

    public Condition(Stage stage, String name, Input input, String defaultText, String hint) {
        this.stage = stage;
        this.name = name;
        this.input = input;
        this.defaultText = defaultText;
        this.hint = hint;
    }

    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public Condition setName(String name) {
        this.name = name;
        return this;
    }

    public Input getInput() {
        return input;
    }

    public Condition setInput(Input input) {
        this.input = input;
        return this;
    }

    public String getDefaultText() {
        return defaultText;
    }

    public Condition setDefaultText(String defaultText) {
        this.defaultText = defaultText;
        return this;
    }

    public String getHint() {
        return hint;
    }

    public Condition setHint(String hint) {
        this.hint = hint;
        return this;
    }

    public Stage getStage() {
        return stage;
    }

    public Condition setStage(Stage stage) {
        this.stage = stage;
        return this;
    }

}
