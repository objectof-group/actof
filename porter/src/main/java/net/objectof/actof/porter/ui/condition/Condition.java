package net.objectof.actof.porter.ui.condition;


public class Condition {

    public enum Stage {

        MATCH {

            public String toString() {
                return "Match";
            }
        },
        BEFORE {

            public String toString() {
                return "Before";
            }
        },
        KEY {

            public String toString() {
                return "Key Transform";
            }
        },
        VALUE {

            public String toString() {
                return "Value Transform";
            }
        },
        AFTER {

            public String toString() {
                return "After";
            }
        };

    }

    public enum Input {
        NONE {

            public String toString() {
                return "None";
            }
        },
        FIELD {

            public String toString() {
                return "Field";
            }
        },
        CODE {

            public String toString() {
                return "Code";
            }
        };
    }

    private Stage stage;
    private String name = "";
    private Input input;
    private String defaultText = "";
    private String hint = "";

    public Condition() {}

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

    public boolean equals(Condition other) {
        if (stage == null && other.stage != null) { return false; }
        if (stage != null && other.stage == null) { return false; }
        if (!stage.equals(other.stage)) { return false; }

        if (input == null && other.input != null) { return false; }
        if (input != null && other.input == null) { return false; }
        if (!input.equals(other.input)) { return false; }

        if (!name.equals(other.name)) { return false; }
        if (!defaultText.equals(other.defaultText)) { return false; }
        if (!hint.equals(other.hint)) { return false; }
        return true;
    }

}
