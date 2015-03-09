package net.objectof.actof.repospy.controllers.navigator.composite.editors.map;


import java.util.Set;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.objectof.actof.repospy.controllers.navigator.ResourceSelectedChange;
import net.objectof.actof.repospy.controllers.navigator.composite.CompositeEntry;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.AbstractEditor;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.ElementsEditor;
import net.objectof.aggr.Mapping;
import net.objectof.model.Resource;


public class MappedEditor extends AbstractEditor implements ElementsEditor {

    private ImageView addImage = new ImageView(new Image(getClass().getResourceAsStream("../icons/add.png")));
    private ImageView remImage = new ImageView(new Image(getClass().getResourceAsStream("../icons/remove.png")));

    VBox pane = new VBox(6);

    ListView<String> list = new ListView<>();
    TextField newkey = new TextField();
    Button add = new Button("", addImage);
    Button rem = new Button("", remImage);
    HBox box = new HBox(3);

    public MappedEditor(CompositeEntry entry) {
        super(entry);

        Button view = new Button("view");
        view.setOnAction(event -> {
            getEntry().getController().getChangeBus()
                    .broadcast(new ResourceSelectedChange((Resource<?>) entry.getFieldValue(), true));

        });

        pane.getChildren().add(view);

    }

    private void updateList() {
        list.getItems().setAll(getElements());
    }

    private void doRemove(String key) {
        if (asMap(getEntry()).keySet().contains(key)) {}
        asMap(getEntry()).remove(key);
    }

    private void doAdd(String key) {
        if (asMap(getEntry()).keySet().contains(key)) { return; }
        asMap(getEntry()).put(key, null);
    }

    @Override
    public Set<String> getElements() {
        return asMap(getEntry()).keySet();
    }

    @SuppressWarnings("unchecked")
    static Mapping<String, Object> asMap(CompositeEntry entry) {
        Object value = entry.getFieldValue();
        if (value == null) {
            value = entry.createFromNull();
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
