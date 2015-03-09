package net.objectof.actof.repospy.controllers.navigator.composite.editors.primitive;


import javafx.scene.Node;
import javafx.scene.control.TextField;
import net.objectof.actof.repospy.controllers.navigator.composite.CompositeEntry;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.AbstractEditor;


public class TextEditor extends AbstractEditor {

    protected TextField field = new TextField();
    protected boolean isFinished = false;

    public static String redborder = "-fx-text-box-border: red; -fx-focus-color: red ;";

    public TextEditor(CompositeEntry entry) {

        super(entry);
        field.setText(entry.toString());
        field.textProperty().addListener((obs, o, n) -> {
            boolean valid = validate(n);
            if (valid) {
                field.setStyle("");
            } else {
                field.setStyle(redborder);
                return;
            }

            getEntry().userInputValue(n);

        });

    }

    public void init() {

    }

    @Override
    protected boolean validate(String text) {
        return true;
    }

    @Override
    public void focus() {
        field.selectAll();
        field.requestFocus();
    }

    @Override
    public Node getNode() {
        return field;
    }

}
