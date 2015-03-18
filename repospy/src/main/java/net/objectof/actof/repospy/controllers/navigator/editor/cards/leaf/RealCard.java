package net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf;


import java.math.BigDecimal;
import java.text.NumberFormat;

import jfxtras.labs.scene.control.BigDecimalField;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;


public class RealCard extends LeafCard {

    public RealCard(ILeafNode entry, boolean capitalize) {
        super(entry, capitalize);

        BigDecimalField field = new BigDecimalField();

        Double value = (Double) entry.getFieldValue();
        if (value == null) {
            value = 0d;
        }
        field.setNumber(new BigDecimal(value));
        field.setFormat(NumberFormat.getNumberInstance());
        field.setStepwidth(new BigDecimal(1));
        field.numberProperty().addListener((obs, oldval, newval) -> {
            entry.userInput(newval.doubleValue());
        });

        setTitleContent(field);

    }

}
