package net.objectof.actof.repospy.controllers.navigator.composite.viewers;


import java.math.BigDecimal;
import java.text.NumberFormat;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import net.objectof.actof.repospy.controllers.navigator.composite.CompositeEntry;


public abstract class NumberViewer extends SimpleViewer {

    public NumberViewer(CompositeEntry entry) {
        super(entry);
    }

    protected abstract StringConverter<BigDecimal> getStringConverter();

    protected abstract NumberFormat getFormat();

    @Override
    public Node getNode() {
        BigDecimal dec = getStringConverter().fromString(getEntry().toString());
        return new Label(getFormat().format(dec));
    }

}
