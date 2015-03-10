package net.objectof.actof.repospy.controllers.navigator.editor.cards;


import javafx.scene.control.TextArea;
import net.objectof.actof.repospy.controllers.navigator.treemodel.ILeafNode;


public class TextCard extends LeafCard {

    public TextCard(ILeafNode entry) {
        super(entry);

        TextArea node = new TextArea();
        node.setText(entry.toString());
        node.textProperty().addListener((obs, o, n) -> {
            getEntry().userInputValue(n);
        });
        setContent(node);
    }

}
