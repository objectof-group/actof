package net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf;


import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import net.objectof.actof.repospy.controllers.navigator.editor.layout.IndexedView;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;


public class TextCard extends LeafCard {

    private Image expimg = new Image(IndexedView.class.getResourceAsStream("../icons/expand.png"));

    public TextCard(ILeafNode entry, boolean capitalize) {
        super(entry, capitalize);

        String contents = "";

        if (entry.getFieldValue() != null) {
            contents = entry.getFieldValue().toString();
        }

        if (contents.contains("\n")) {
            createTextArea(entry);
        } else {
            createTextField(entry);
        }
    }

    private void createTextField(ILeafNode entry) {
        TextField node = new TextField();
        node.setText(textFromLeaf(entry));
        node.textProperty().addListener((obs, o, n) -> {
            getEntry().userInputString(n);
        });
        HBox.setHgrow(node, Priority.ALWAYS);

        Button expand = new Button("", new ImageView(expimg));
        expand.setTooltip(new Tooltip("Expand to multiline text field"));
        expand.getStyleClass().add("tool-bar-button");
        expand.setOnAction(action -> {
            createTextArea(entry);
        });

        HBox content = new HBox(6, node, expand);

        setContent(content, true);
    }

    private void createTextArea(ILeafNode entry) {
        TextArea node = new TextArea();
        node.setText(textFromLeaf(entry));
        node.textProperty().addListener((obs, o, n) -> {
            getEntry().userInputString(n);
        });
        setContent(node);
    }

    private String textFromLeaf(ILeafNode entry) {
        if (entry.getFieldValue() == null) { return null; }
        return entry.getFieldValue().toString();
    }
}
