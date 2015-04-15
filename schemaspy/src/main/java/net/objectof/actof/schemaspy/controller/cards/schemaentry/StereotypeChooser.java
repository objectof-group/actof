package net.objectof.actof.schemaspy.controller.cards.schemaentry;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;
import net.objectof.actof.common.controller.schema.schemaentry.SchemaEntry;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.model.Stereotype;


public class StereotypeChooser extends ChoiceBox<Stereotype> {

    public StereotypeChooser(SchemaEntry entry) {

        ObservableList<Stereotype> stereotypes = FXCollections.observableArrayList(Stereotype.values());
        stereotypes.sort((a, b) -> a.toString().compareTo(b.toString()));
        setItems(stereotypes);
        getSelectionModel().select(entry.getStereotype());
        getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            entry.setStereotype(n);
        });

        setConverter(new StringConverter<Stereotype>() {

            @Override
            public String toString(Stereotype st) {
                return RepoUtils.prettyPrint(st);
            }

            @Override
            public Stereotype fromString(String string) {
                return null;
            }
        });

    }

}
