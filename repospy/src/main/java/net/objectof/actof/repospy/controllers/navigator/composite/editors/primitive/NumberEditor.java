package net.objectof.actof.repospy.controllers.navigator.composite.editors.primitive;


import java.math.BigDecimal;
import java.text.NumberFormat;

import javafx.scene.Node;
import javafx.util.StringConverter;
import jfxtras.labs.scene.control.BigDecimalField;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.AbstractEditor;
import net.objectof.actof.repospy.controllers.navigator.kind.ILeafNode;


public abstract class NumberEditor extends AbstractEditor {

    BigDecimalField field = new BigDecimalField();
    protected boolean isFinished = false;

    public NumberEditor(ILeafNode entry) {
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

    }

    public boolean inline() {
        return true;
    }

    private String val(BigDecimal dec) {
        return getStringConverter().toString(dec);
    }

    protected abstract StringConverter<BigDecimal> getStringConverter();

    protected abstract NumberFormat getFormat();

    @Override
    public void focus() {
        field.requestFocus();
    }

    @Override
    public Node getNode() {
        return field;
    }

    @Override
    protected boolean validate(String text) {
        try {
            getStringConverter().fromString(text);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean expand() {
        return false;
    }

}
