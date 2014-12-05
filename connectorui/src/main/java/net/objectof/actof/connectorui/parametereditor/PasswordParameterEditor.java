package net.objectof.actof.connectorui.parametereditor;


import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import net.objectof.actof.connector.parameter.Parameter;


public class PasswordParameterEditor extends PasswordField implements ParameterEditor {

    private Parameter param;

    public PasswordParameterEditor(Parameter param) {
        this.param = param;

        setText(param.getValue());
        setPromptText(param.getTitle());

        setOnKeyReleased(event -> {
            param.setValue(getText());
            if (!getText().equals(param.getValue())) {
                setText(param.getValue());
            }
        });


    }

    @Override
    public Node asNode() {
        return this;
    }

    @Override
    public Parameter getParameter() {
        return param;
    }

    @Override
    public void setCreate(boolean create) {}

}
