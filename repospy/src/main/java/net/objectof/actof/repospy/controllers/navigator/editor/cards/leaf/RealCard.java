package net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf;


import java.math.BigDecimal;
import java.text.NumberFormat;

import jfxtras.labs.scene.control.BigDecimalField;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;


public class RealCard extends LeafCard {

    BigDecimalField field = new BigDecimalField();

    public RealCard(ILeafNode entry, boolean capitalize) {
        super(entry, capitalize);

        field.setFormat(NumberFormat.getNumberInstance());
        field.setStepwidth(new BigDecimal(1));
        field.numberProperty().addListener((obs, oldval, newval) -> {
            if (isUpdating()) { return; }
            entry.setValue(newval.doubleValue());
        });

        updateFromEntry();
        setTitleContent(field);

    }

    @Override
    public void updateUIFromEntry() {
        Double value = (Double) getEntry().getFieldValue();
        if (value == null) {
            value = 0d;
        }
        field.setNumber(new BigDecimal(value));
    }

}
