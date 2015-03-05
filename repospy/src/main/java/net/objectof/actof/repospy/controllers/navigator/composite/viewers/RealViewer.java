package net.objectof.actof.repospy.controllers.navigator.composite.viewers;


import java.math.BigDecimal;
import java.text.NumberFormat;

import javafx.util.StringConverter;
import net.objectof.actof.repospy.controllers.navigator.composite.CompositeEntry;


public class RealViewer extends NumberViewer {

    public RealViewer(CompositeEntry entry) {
        super(entry);
    }

    @Override
    protected StringConverter<BigDecimal> getStringConverter() {
        return new StringConverter<BigDecimal>() {

            @Override
            public String toString(BigDecimal dec) {
                return dec.doubleValue() + "";
            }

            @Override
            public BigDecimal fromString(String string) {
                return new BigDecimal(string);
            }
        };
    }

    @Override
    protected NumberFormat getFormat() {
        return NumberFormat.getNumberInstance();
    }

}
