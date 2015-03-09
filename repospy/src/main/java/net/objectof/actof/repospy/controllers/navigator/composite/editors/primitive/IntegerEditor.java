package net.objectof.actof.repospy.controllers.navigator.composite.editors.primitive;


import java.math.BigDecimal;
import java.text.NumberFormat;

import javafx.util.StringConverter;
import net.objectof.actof.repospy.controllers.navigator.kind.LeafEntry;


public class IntegerEditor extends NumberEditor {

    public IntegerEditor(LeafEntry entry) {
        super(entry);
    }

    @Override
    protected StringConverter<BigDecimal> getStringConverter() {
        return new StringConverter<BigDecimal>() {

            @Override
            public String toString(BigDecimal dec) {
                return dec.longValue() + "";
            }

            @Override
            public BigDecimal fromString(String string) {
                return new BigDecimal(string);
            }
        };
    }

    @Override
    protected NumberFormat getFormat() {
        return NumberFormat.getIntegerInstance();
    }

}
