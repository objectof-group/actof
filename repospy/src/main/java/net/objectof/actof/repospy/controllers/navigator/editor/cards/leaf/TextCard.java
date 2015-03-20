package net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf;


import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;


public class TextCard extends LeafCard {

    private Image expimg = new Image(TextCard.class.getResourceAsStream("icons/expand.png"));
    private TextInputControl textBox;

    public TextCard(ILeafNode entry) {
        super(entry);

        String contents = "";

        if (entry.getFieldValue() != null) {
            contents = entry.getFieldValue().toString();
        }

        if (contents.contains("\n")) {
            createTextArea(entry);
        } else {
            createTextField(entry);
        }

        updateFromEntry();
    }

    private void createTextField(ILeafNode entry) {
        textBox = new TextField();
        textBox.textProperty().addListener((obs, o, n) -> {
            if (isUpdating()) { return; }
            getEntry().setFieldValue(n);
        });
        HBox.setHgrow(textBox, Priority.ALWAYS);

        Button expand = new Button("", new ImageView(expimg));
        expand.setTooltip(new Tooltip("Expand to multiline text field"));
        expand.getStyleClass().add("tool-bar-button");
        expand.setOnAction(action -> {
            createTextArea(entry);
        });

        HBox content = new HBox(6, textBox, expand);
        setContent(content, true);
    }

    private void createTextArea(ILeafNode entry) {
        textBox = new TextArea();
        textBox.textProperty().addListener((obs, o, n) -> {
            if (isUpdating()) { return; }
            getEntry().setFieldValue(n);
        });
        setContent(textBox);
        textBox.requestFocus();
    }

    @Override
    public void updateUIFromEntry() {
        String value = (String) getEntry().getFieldValue();
        if (value == null) {
            textBox.setPromptText("None");
        } else {
            textBox.setPromptText("");
        }
        textBox.setText(value);
    }

}
