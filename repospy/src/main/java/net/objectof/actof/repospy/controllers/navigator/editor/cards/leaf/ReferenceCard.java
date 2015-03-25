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
import net.objectof.actof.widgets.KeyValuePane;
import net.objectof.aggr.Composite;
import net.objectof.model.Resource;
import net.objectof.model.impl.IKind;


public class ReferenceCard extends LeafCard {

    private ChoiceBox<Resource<?>> refs;
    private KeyValuePane fields;

    public ReferenceCard(ILeafNode entry) {
        super(entry);

        refs = new ChoiceBox<>();
        refs.setConverter(new StringConverter<Resource<?>>() {

            @Override
            public String toString(Resource<?> res) {
                return RepoUtils.prettyPrint(res);
            }

            @Override
            public Resource<?> fromString(String string) {
                return null;
            }
        });

        fields = new KeyValuePane();
        fields.setPadding(new Insets(10, 0, 0, 20));
        ColumnConstraints constraints;
        constraints = new ColumnConstraints();
        constraints.setMinWidth(100);
        fields.getColumnConstraints().add(constraints);

        updateFromEntry();

        refs.valueProperty().addListener((obs, oldval, newval) -> {
            updateGrid();
            if (isUpdating()) { return; }
            getEntry().setFieldValue(newval);
        });
        setTitleContent(refs);
        setContent(fields);

    }

    private void updateGrid() {
        Resource<?> res = refs.getSelectionModel().getSelectedItem();
        fields.clear();
        if (res == null) { return; }

        Composite composite = (Composite) res;
        for (String key : composite.keySet()) {
            fields.put(key, RepoUtils.prettyPrint(composite.get(key)));
        }

    }

    private List<Resource<?>> getElements() {
        IKind<?> ikind = (IKind<?>) getEntry().getCanonicalKind();
        String title = ikind.getTitle();
        Iterable<Resource<?>> resiter = getEntry().getController().repository.getStagingTx().enumerate(title);
        List<Resource<?>> resources = StreamSupport.stream(resiter.spliterator(), false).collect(Collectors.toList());
        resources.add(0, null);
        return resources;
    }

    @Override
    public void updateUIFromEntry() {
        refs.getItems().setAll(getElements());
        Resource<?> selected = (Resource<?>) getEntry().getFieldValue();
        if (selected == null) {
            refs.getSelectionModel().select(0);
        } else {
            refs.getSelectionModel().select(selected);
        }
        updateGrid();
    }

}