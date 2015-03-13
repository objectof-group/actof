package net.objectof.actof.schemaspy.controller.cards;


import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import net.objectof.actof.common.controller.schema.AttributeEntry;
import net.objectof.actof.common.controller.schema.schemaentry.SchemaEntry;
import net.objectof.actof.schemaspy.SchemaSpyController;
import net.objectof.model.Stereotype;


public class StereotypeCard extends SchemaSpyCard {

    ChoiceBox<Stereotype> choice;

    @Override
    public List<AttributeEntry> attributes(List<AttributeEntry> unhandled) {
        return new ArrayList<>();
    }

    @Override
    public boolean appliesTo(SchemaEntry schemaEntry, List<AttributeEntry> unhandled) {
        return true;
    }

    @Override
    public void init(SchemaSpyController schemaspy, List<AttributeEntry> unhandled) {

        ObservableList<Stereotype> stereotypes = FXCollections.observableArrayList(Stereotype.values());
        stereotypes.sort((a, b) -> a.toString().compareTo(b.toString()));
        choice = new ChoiceBox<>(stereotypes);
        choice.getSelectionModel().select(getSchemaEntry().getStereotype());
        choice.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            getSchemaEntry().setStereotype(n);
        });

    }

    @Override
    protected String getName() {
        return "Stereotype";
    }

    @Override
    protected Node getNode() {
        return choice;
    }

    protected boolean inline() {
        return true;
    }

}
