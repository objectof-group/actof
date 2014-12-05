package net.objectof.actof.repospy.controller.navigator.composite.editors;


import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import net.objectof.actof.repospy.controller.navigator.composite.CompositeEntry;


public class TextEditor extends AbstractEditor {

    protected TextField field = new TextField();
    protected boolean isFinished = false;

    public static String redborder = "-fx-text-box-border: red; -fx-focus-color: red ;";

    public TextEditor(CompositeEntry entry) {

        super(entry);
        field.setText(entry.toString());

        field.setOnKeyPressed(event -> {
            if (isFinished) { return; }

            String text = field.getText();
            boolean valid = validate(text);

            if (event.getCode() == KeyCode.ENTER) {
                if (valid) {
                    isFinished = true;
                    onComplete.accept(field.getText());
                } else {
                    isFinished = true;
                    onCancel.run();
                }
            } else if (event.getCode() == KeyCode.ESCAPE) {
                isFinished = true;
                onCancel.run();
            }
        });


        field.setOnKeyReleased((KeyEvent t) -> {
            if (isFinished) { return; }

            String text = field.getText();
            boolean valid = validate(text);

            if (valid) {
                field.setStyle("");
            } else {
                field.setStyle(redborder);
            }
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
