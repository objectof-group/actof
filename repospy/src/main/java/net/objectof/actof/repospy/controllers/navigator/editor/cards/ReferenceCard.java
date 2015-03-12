package net.objectof.actof.repospy.controllers.navigator.editor.cards;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.controllers.navigator.treemodel.ILeafNode;
import net.objectof.model.Resource;
import net.objectof.model.impl.IKind;


public class ReferenceCard extends LeafCard {

    public ReferenceCard(ILeafNode entry, boolean capitalize) {
        super(entry, capitalize);

        ChoiceBox<Resource<?>> refs = new ChoiceBox<>();
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

    }

    public List<Resource<?>> getElements() {
        IKind<?> ikind = (IKind<?>) getEntry().kind;
        String title = ikind.getTitle();
        Iterable<Resource<?>> resiter = getEntry().getController().repository.getStagingTx().enumerate(title);
        List<Resource<?>> resources = StreamSupport.stream(resiter.spliterator(), false).collect(Collectors.toList());
        return resources;
    }

}