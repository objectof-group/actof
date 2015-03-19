package net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf;


import java.math.BigDecimal;
import java.text.NumberFormat;

import jfxtras.labs.scene.control.BigDecimalField;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;


public class IntegerCard extends LeafCard {

    BigDecimalField field = new BigDecimalField();

    public IntegerCard(ILeafNode entry) {
        super(entry);

        field.setFormat(NumberFormat.getIntegerInstance());
        field.setStepwidth(new BigDecimal(1));
        field.numberProperty().addListener((obs, oldval, newval) -> {
            if (isUpdating()) { return; }
            entry.setFieldValue(newval.longValue());
        });

        updateFromEntry();
        setTitleContent(field);

    }

    @Override
    public void updateUIFromEntry() {
        Long value = (Long) getEntry().getFieldValue();
        if (value == null) {
            value = 0l;
        }
        field.setNumber(new BigDecimal(value));
    }

}
