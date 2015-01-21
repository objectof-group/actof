package net.objectof.actof.schemaspy.controller.cards;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import net.objectof.actof.common.controller.change.Change;
import net.objectof.actof.common.controller.schema.AttributeEntry;
import net.objectof.actof.common.controller.schema.schemaentry.SchemaEntry;
import net.objectof.actof.schemaspy.SchemaSpyController;

import org.controlsfx.dialog.DialogStyle;
import org.controlsfx.dialog.Dialogs;


public class AttributesCard extends Card {

    BorderPane top;
    TableView<AttributeEntry> table;
    TableColumn<AttributeEntry, String> namespace, attrname, attrvalue;
    Button add, remove;
    SchemaEntry schemaEntry;

    @Override
    public List<AttributeEntry> attributes(List<AttributeEntry> unhandled) {
        return new ArrayList<>(unhandled);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init(SchemaSpyController schemaspy, SchemaEntry entry, List<AttributeEntry> unhandled) {

        this.schemaEntry = entry;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AttributeCard.fxml"));
        try {
            top = loader.load();
            // table.setPrefWidth(Region.USE_COMPUTED_SIZE);

            table = (TableView<AttributeEntry>) loader.getNamespace().get("table");
            namespace = (TableColumn<AttributeEntry, String>) loader.getNamespace().get("namespace");
            attrname = (TableColumn<AttributeEntry, String>) loader.getNamespace().get("name");
            attrvalue = (TableColumn<AttributeEntry, String>) loader.getNamespace().get("value");
            add = (Button) loader.getNamespace().get("add");
            remove = (Button) loader.getNamespace().get("remove");

            table.getItems().setAll(unhandled);

            namespace.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNamespace()));
            namespace.setCellFactory(data -> {
                List<String> namespaces = new ArrayList<>();
                namespaces.add(null);
                namespaces.addAll(schemaspy.getSchema().getNamespaces().keySet());
                return new ComboBoxTableCell<>(FXCollections.observableArrayList(namespaces));

            });
            namespace.setOnEditCommit(event -> event.getRowValue().setNamespace(event.getNewValue()));

            attrname.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            attrname.setCellFactory(data -> new TextFieldTableCell<>(new StringStringConverter()));
            attrname.setOnEditCommit(event -> event.getRowValue().setName(event.getNewValue()));

            attrvalue.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getValue()));
            attrvalue.setCellFactory(data -> new TextFieldTableCell<>(new StringStringConverter()));
            attrvalue.setOnEditCommit(event -> event.getRowValue().setValue(event.getNewValue()));

            add.setOnAction(event -> onAdd());
            remove.setOnAction(event -> onRemove());

            table.getSelectionModel().selectedItemProperty().addListener((ovs, o, n) -> {
                setButtonState();
            });
            setButtonState();

            schemaspy.getChangeBus().listen(this::onChange);

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected String getName() {
        return "Attributes";
    }

    @Override
    protected Node getNode() {
        return top;
    }

    @Override
    public boolean appliesTo(SchemaEntry schemaEntry, List<AttributeEntry> unhandled) {
        return true;
    }

    private void setButtonState() {
        remove.setDisable(table.getSelectionModel().getSelectedItem() == null);
    }

    private void onRemove() {
        AttributeEntry attr = table.getSelectionModel().getSelectedItem();
        if (attr == null) { return; }
        schemaEntry.removeAttribute(attr);
    }

    private void onAdd() {

        Optional<String> response = Dialogs.create().owner(getUI()).title("Create Node").message("Name of node")
                .lightweight().style(DialogStyle.UNDECORATED).showTextInput("node");

        if (!response.isPresent()) { return; }

        schemaEntry.addAttribute(response.get(), "value");
    }

    private void onChange(Change change) {
        /*
         * change.when(AttributeRemovalChange.class, removal -> {
         * table.getItems().remove(removal.getAttribute()); });
         * 
         * change.when(AttributeCreationChange.class, addition -> {
         * table.getItems().add(addition.getAttribute()); });
         */
    }

}

class StringStringConverter extends StringConverter<String> {

    @Override
    public String toString(String object) {
        return object;
    }

    @Override
    public String fromString(String string) {
        return string;
    }

}
