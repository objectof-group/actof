package net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf;


import java.math.BigDecimal;
import java.text.NumberFormat;

import jfxtras.labs.scene.control.BigDecimalField;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;


public class IntegerCard extends LeafCard {

    public IntegerCard(ILeafNode entry, boolean capitalize) {
        super(entry, capitalize);

        BigDecimalField field = new BigDecimalField();

        Long value = (Long) entry.getFieldValue();
        if (value == null) {
            value = 0l;
        }
        field.setNumber(new BigDecimal(value));
        field.setFormat(NumberFormat.getIntegerInstance());
        field.setStepwidth(new BigDecimal(1));
        field.numberProperty().addListener((obs, oldval, newval) -> {
            entry.userInput(newval.longValue());
        });

        setTitleContent(field);

    }

}
