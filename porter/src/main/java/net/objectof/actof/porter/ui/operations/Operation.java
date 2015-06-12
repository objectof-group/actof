package net.objectof.actof.porter.ui.operations;


import javafx.beans.property.SimpleStringProperty;
import net.objectof.actof.porter.ui.action.Action;
import net.objectof.actof.porter.ui.condition.Condition;
import net.objectof.actof.porter.ui.condition.Condition.Stage;


public class Operation {

    private Action action;
    private Condition condition;

    /**
     * For JavaFX UI purposes. This should be the same as a call to toString
     */
    private SimpleStringProperty title;

    public Operation() {
        title = new SimpleStringProperty("");
        // WHY IS THIS NECESSARY? THE OPERATION EDITOR LISTVIEW WON'T UPDATE
        // WITHOUT IT!
        title.addListener((obs, o, n) -> {});
    }

    public Operation(Condition condition, Action action) {
        this();
        this.action = action;
        this.condition = condition;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        String arrow = " \u279E ";
        if (getCondition() == null) { return "Invalid Operation"; }

        Stage stage = getCondition().getStage();
        String name = getCondition().getName();

        if (stage == null && (name == null || name.length() == 0)) { return "Empty Operation"; }
        if (stage == null) { return "?" + arrow + name; }
        if (name == null || name.length() == 0) { return stage + arrow + "?"; }

        return getCondition().getStage().toString() + " \u279E " + getCondition().getName();
    }

    public final SimpleStringProperty titleProperty() {
        return this.title;
    }

    public final java.lang.String getTitle() {
        return this.titleProperty().get();
    }

    public final void setTitle(final java.lang.String newTitle) {
        this.titleProperty().set(newTitle);
    }

}
