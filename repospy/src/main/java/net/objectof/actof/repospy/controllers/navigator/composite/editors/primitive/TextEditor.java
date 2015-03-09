package net.objectof.actof.repospy.controllers.navigator.composite.editors.primitive;


import javafx.scene.Node;
import javafx.scene.control.TextArea;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.AbstractEditor;
import net.objectof.actof.repospy.controllers.navigator.kind.ILeafNode;


public class TextEditor extends AbstractEditor {

    protected TextArea node = new TextArea();
    protected boolean isFinished = false;

    public static String redborder = "-fx-text-box-border: red; -fx-focus-color: red ;";

    public TextEditor(ILeafNode entry) {
        super(entry);

        node.setText(entry.toString());
        node.textProperty().addListener((obs, o, n) -> {
            boolean valid = validate(n);
            if (valid) {
                node.setStyle("");
            } else {
                node.setStyle(redborder);
                return;
            }

            getEntry().userInputValue(n);

        });

    }

    public boolean inline() {
        return false;
    }

    public void init() {

    }

    @Override
    protected boolean validate(String text) {
        return true;
    }

    @Override
    public void focus() {
        node.selectAll();
        node.requestFocus();
    }

    @Override
    public Node getNode() {
        return node;
    }

}
