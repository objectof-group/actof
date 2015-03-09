package net.objectof.actof.repospy.controllers.navigator.composite.editors.aggregate;


import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.AbstractEditor;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.ElementsEditor;
import net.objectof.actof.repospy.controllers.navigator.kind.ILeafNode;


public abstract class AbstractComboboxEditor extends AbstractEditor implements ElementsEditor {

    protected ComboBox<String> field = new ComboBox<String>();
    private String redborder = "-fx-combo-box-border: red; -fx-focus-color: red;";

    public AbstractComboboxEditor(ILeafNode entry, boolean editable) {
        super(entry);

        field.setEditable(editable);
        field.setMaxWidth(100000);

        populate();
        addListeners();

    }

    protected String getValue() {
        return field.isEditable() ? field.getEditor().getText() : field.getValue();
    }

    private void populate() {

        // set all combobox items
        field.getItems().clear();
        for (String element : getElements()) {
            field.getItems().add(element);
        }

        field.setValue(defaultValue());

    }

    protected String defaultValue() {

        // determine what the current value should be
        Object currentValue = getEntry().getFieldValue();
        if (currentValue != null) {
            return RepoUtils.resToString(currentValue);
        } else if (field.getItems().size() > 0) { return field.getItems().get(0); }
        return null;

    }

    @Override
    public void focus() {
        field.requestFocus();
    }

    @Override
    public Node getNode() {
        return field;
    }

    protected void validationUI(boolean valid) {
        if (valid) {
            field.setStyle("");
        } else {
            field.setStyle(redborder);
        }
    }

    protected abstract void addListeners();

}
