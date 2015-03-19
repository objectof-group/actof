package net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf;


import javafx.scene.control.CheckBox;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;


public class BooleanCard extends LeafCard {

    CheckBox check;

    public BooleanCard(ILeafNode entry) {
        super(entry);

        check = new CheckBox();
        check.selectedProperty().addListener(change -> {
            if (isUpdating()) { return; }
            getEntry().setFieldValue(check.selectedProperty().getValue());
        });
        setTitleContent(check);
        updateFromEntry();

    }

    @Override
    public void updateUIFromEntry() {
        Boolean value = (Boolean) getEntry().getFieldValue();
        if (value == null) {
            value = false;
        }
        check.setSelected(value);
    }
}
