package net.objectof.actof.repospy.controllers.navigator.editor.cards;


import java.math.BigDecimal;
import java.text.NumberFormat;

import javafx.util.StringConverter;
import jfxtras.labs.scene.control.BigDecimalField;
import net.objectof.actof.repospy.controllers.navigator.treemodel.ILeafNode;


public abstract class NumberCard extends LeafCard {

    BigDecimalField field = new BigDecimalField();
    protected boolean isFinished = false;

    public NumberCard(ILeafNode entry) {
        super(entry);

        field.setNumber(new BigDecimal(entry.toString()));
        field.setFormat(getFormat());
        field.setStepwidth(new BigDecimal(1));

        field.numberProperty().addListener((obs, oldval, newval) -> {
            if (validate(val(newval))) {
                entry.userInputValue(val(newval));
            } else {
                field.setNumber(oldval);
            }
        });

        setTitleContent(field);

    }

    private String val(BigDecimal dec) {
        return getStringConverter().toString(dec);
    }

    protected abstract StringConverter<BigDecimal> getStringConverter();

    protected abstract NumberFormat getFormat();

    protected boolean validate(String text) {
        try {
            getStringConverter().fromString(text);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

}
