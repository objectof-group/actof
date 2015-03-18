package net.objectof.actof.widgets;


import java.util.LinkedHashMap;
import java.util.Map;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;


public class PropertiesPane extends GridPane {

    private Map<String, String> properties = new LinkedHashMap<>();
    private SimpleStringProperty keyStyle = new SimpleStringProperty();
    private SimpleStringProperty valueStyle = new SimpleStringProperty();
    private HPos keyAlignment = HPos.LEFT;

    public PropertiesPane() {}

    public void addProperty(String key, String value) {
        properties.put(key, value);
        setHgap(10);
        updateUI();
    }

    public void removeProperty(String key) {
        properties.remove(key);
        updateUI();
    }

    public void clearProperties() {
        properties.clear();
        updateUI();
    }

    private void updateUI() {

        getChildren().clear();
        int row = 0;

        for (String key : properties.keySet()) {
            String value = properties.get(key);

            Label lKey = new Label(key);
            Label lValue = new Label(value);

            GridPane.setHalignment(lKey, keyAlignment);

            add(lKey, 0, row);
            add(lValue, 1, row);
            row++;

            lKey.styleProperty().bind(keyStyle);
            lValue.styleProperty().bind(valueStyle);

        }
    }

    public String getKeyStyle() {
        return keyStyle.get();
    }

    public void setKeyStyle(String keyStyle) {
        this.keyStyle.set(keyStyle);
    }

    public Property<String> keyStyleProperty() {
        return keyStyle;
    }

    public String getValueStyle() {
        return valueStyle.get();
    }

    public void setValueStyle(String valueStyle) {
        this.valueStyle.set(valueStyle);
    }

    public Property<String> valueStyleProperty() {
        return valueStyle;
    }

    public HPos getKeyAlignment() {
        return keyAlignment;
    }

    public void setKeyAlignment(HPos keyAlignment) {
        this.keyAlignment = keyAlignment;
        updateUI();
    }

}
