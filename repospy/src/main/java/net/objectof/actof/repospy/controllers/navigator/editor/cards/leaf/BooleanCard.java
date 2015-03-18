package net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf;


import javafx.scene.control.CheckBox;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;


public class BooleanCard extends LeafCard {

    public BooleanCard(ILeafNode entry, boolean capitalize) {
        super(entry, capitalize);

        CheckBox check = new CheckBox();
        check.selectedProperty().addListener(change -> {
            getEntry().userInput(check.selectedProperty().getValue());
        });

        setTitleContent(check);

    }

}
