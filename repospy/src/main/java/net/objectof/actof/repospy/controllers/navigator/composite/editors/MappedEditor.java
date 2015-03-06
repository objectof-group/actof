package net.objectof.actof.repospy.controllers.navigator.composite.editors;


import java.util.Set;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import net.objectof.actof.repospy.controllers.navigator.composite.CompositeEntry;
import net.objectof.aggr.Mapping;


public class MappedEditor extends AbstractEditor implements ElementsEditor {

    private ImageView addImage = new ImageView(new Image(getClass().getResourceAsStream("icons/add.png")));
    private ImageView remImage = new ImageView(new Image(getClass().getResourceAsStream("icons/remove.png")));

    VBox pane = new VBox(6);
    ListView<String> list = new ListView<>();
    TextField newkey = new TextField();
    Button add = new Button("", addImage);
    Button rem = new Button("", remImage);
    HBox box = new HBox(3);

    public MappedEditor(CompositeEntry entry) {
        super(entry);

        box.getChildren().addAll(newkey, add, rem);
        HBox.setHgrow(newkey, Priority.ALWAYS);

        list.getItems().addAll(getElements());
        list.setPrefHeight(300);
        list.setPrefWidth(300);
        VBox.setVgrow(list, Priority.ALWAYS);

        pane.getChildren().addAll(box, list);

        add.setOnAction(event -> {
            doAdd(newkey.getText());
            updateList();
            newkey.setText("");
        });

        rem.setOnAction(event -> {
            String key = list.getSelectionModel().getSelectedItem();
            if (key == null) { return; }
            doRemove(key);
            updateList();
        });

    }

    private void updateList() {
        list.getItems().setAll(getElements());
    }

    private void doRemove(String key) {
        if (asMap().keySet().contains(key)) {
            modified();
        }
        asMap().remove(key);
        modified();
    }

    private void doAdd(String key) {
        if (asMap().keySet().contains(key)) { return; }
        asMap().put(key, null);
        modified();
    }

    @Override
    public Set<String> getElements() {
        return asMap().keySet();
    }

    @SuppressWarnings("unchecked")
    private Mapping<String, Object> asMap() {
        Object value = getEntry().getFieldValue();
        if (value == null) {
            value = getEntry().createFromNull();
        }
        return (Mapping<String, Object>) value;
    }

    @Override
    protected boolean validate(String input) {
        return true;
    }

    @Override
    public void focus() {
        // TODO Auto-generated method stub

    }

    @Override
    public Node getNode() {
        return pane;
    }

}
