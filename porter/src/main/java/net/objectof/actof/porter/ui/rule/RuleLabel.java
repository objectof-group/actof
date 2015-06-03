package net.objectof.actof.porter.ui.rule;


import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;


public class RuleLabel extends BorderPane {

    private String title;
    private TextField field;
    private Label label;

    public RuleLabel(String text) {

        this.title = text;
        field = new TextField(text);
        label = new Label(text);
        label.setMinWidth(64);

        label.setOnMouseClicked(event -> {
            field.setText(title);
            setCenter(field);
        });

        field.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.ENTER) {
                title = field.getText();
                label.setText(title);
                setCenter(label);
            }

            if (event.getCode() == KeyCode.ESCAPE) {
                label.setText(title);
                setCenter(label);
            }

        });

        setCenter(label);

    }

    public String getText() {
        return title;
    }

    public void setText(String text) {
        title = text;
        field.setText(text);
        label.setText(text);
    }

}
