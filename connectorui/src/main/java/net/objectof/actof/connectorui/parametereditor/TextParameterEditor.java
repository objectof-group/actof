package net.objectof.actof.connectorui.parametereditor;


import javafx.scene.Node;
import javafx.scene.control.TextField;
import net.objectof.actof.connector.parameter.Parameter;


public class TextParameterEditor extends TextField implements ParameterEditor {

    Parameter param;

    public TextParameterEditor(Parameter param) {
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
    public Parameter getParameter() {
        return param;
    }


    @Override
    public Node asNode() {
        return this;
    }

    @Override
    public void setCreate(boolean create) {}

}
