package net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf;


import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;


public class RealCard extends LeafCard {

    Spinner<Double> spinner;

    public RealCard(ILeafNode entry) {
        super(entry);

        spinner = new Spinner<>(new SpinnerValueFactory.DoubleSpinnerValueFactory(Double.MIN_VALUE, Double.MAX_VALUE));
        spinner.valueProperty().addListener(change -> {
            if (isUpdating()) { return; }
            entry.setFieldValue(spinner.getValue());
        });

        updateFromEntry();
        setTitleContent(spinner);

    }

    @Override
    public void updateUIFromEntry() {
        Double value = (Double) getEntry().getFieldValue();
        if (value == null) {
            value = 0d;
        }
        spinner.getValueFactory().setValue(value);
    }

}
