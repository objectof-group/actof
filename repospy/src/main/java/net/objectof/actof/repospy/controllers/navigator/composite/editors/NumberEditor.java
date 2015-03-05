package net.objectof.actof.repospy.controllers.navigator.composite.editors;


import java.math.BigDecimal;
import java.text.NumberFormat;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import jfxtras.labs.scene.control.BigDecimalField;
import net.objectof.actof.repospy.controllers.navigator.composite.CompositeEntry;


public abstract class NumberEditor extends AbstractEditor {

    BigDecimalField field = new BigDecimalField();
    protected boolean isFinished = false;

    public NumberEditor(CompositeEntry entry) {
        super(entry);

        field.setNumber(new BigDecimal(entry.toString()));
        field.setFormat(getFormat());
        field.setStepwidth(new BigDecimal(1));

        field.setOnKeyReleased((KeyEvent t) -> {
            if (isFinished) { return; }

            String text = val(field.getNumber());
            boolean valid = validate(text);
            if (valid) {
                field.setStyle("");
            }

            if (t.getCode() == KeyCode.ENTER) {
                if (valid) {
                    isFinished = true;
                    onComplete.accept(text);
                } else {
                    isFinished = false;
                    field.setStyle(TextEditor.redborder);
                }
            } else if (t.getCode() == KeyCode.ESCAPE) {
                isFinished = true;
                onCancel.run();
            }

        });

        field.numberProperty().addListener((obs, oldval, newval) -> {
            if (!validate(val(newval))) {
                field.setNumber(oldval);
            }
        });

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

}
