package net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf;


import java.math.BigDecimal;
import java.text.NumberFormat;

import javafx.util.StringConverter;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;


public class IntegerCard extends NumberCard {

    public IntegerCard(ILeafNode entry, boolean capitalize) {
        super(entry, capitalize);
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
