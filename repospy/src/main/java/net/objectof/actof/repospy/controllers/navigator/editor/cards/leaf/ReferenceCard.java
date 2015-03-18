package net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.ColumnConstraints;
import javafx.util.StringConverter;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;
import net.objectof.actof.widgets.PropertiesPane;
import net.objectof.aggr.Composite;
import net.objectof.model.Resource;
import net.objectof.model.impl.IKind;


public class ReferenceCard extends LeafCard {

    private ChoiceBox<Resource<?>> refs;
    private PropertiesPane fields;
    private String keyStyle = "-fx-text-fill: #999999;";
    private String valueStyle = "-fx-text-fill: #555555;";

    public ReferenceCard(ILeafNode entry, boolean capitalize) {
        super(entry, capitalize);

        refs = new ChoiceBox<>();
        refs.setConverter(new StringConverter<Resource<?>>() {

            @Override
            public String toString(Resource<?> res) {
                return RepoUtils.prettyPrintRes(res);
            }

            @Override
            public Resource<?> fromString(String string) {
                return null;
            }
        });

        refs.getItems().setAll(getElements());
        refs.getSelectionModel().select((Resource<?>) entry.getFieldValue());

        refs.valueProperty().addListener((obs, oldval, newval) -> {
            getEntry().userInputResource(newval);
        });
        setTitleContent(refs);

        fields = new PropertiesPane();
        fields.setPadding(new Insets(10, 0, 0, 20));
        fields.setKeyStyle(keyStyle);
        fields.setValueStyle(valueStyle);
        ColumnConstraints constraints;
        constraints = new ColumnConstraints();
        constraints.setMinWidth(100);
        fields.getColumnConstraints().add(constraints);

        refs.getSelectionModel().selectedItemProperty().addListener(change -> {
            updateGrid();
        });
        setContent(fields);

        updateGrid();

    }

    private void updateGrid() {
        Resource<?> res = refs.getSelectionModel().getSelectedItem();
        fields.clearProperties();
        if (res == null) { return; }

        Composite composite = (Composite) res;
        for (String key : composite.keySet()) {
            fields.addProperty(key, RepoUtils.prettyPrint(composite.get(key)));
        }

    }

    private List<Resource<?>> getElements() {
        IKind<?> ikind = (IKind<?>) getEntry().kind;
        String title = ikind.getTitle();
        Iterable<Resource<?>> resiter = getEntry().getController().repository.getStagingTx().enumerate(title);
        List<Resource<?>> resources = StreamSupport.stream(resiter.spliterator(), false).collect(Collectors.toList());
        return resources;
    }
}