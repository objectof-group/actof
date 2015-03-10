package net.objectof.actof.repospy.controllers.navigator.editor.cards;


import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javafx.scene.control.ChoiceBox;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.controllers.navigator.treemodel.ILeafNode;
import net.objectof.model.Resource;
import net.objectof.model.impl.IKind;


public class ReferenceCard extends LeafCard {

    public ReferenceCard(ILeafNode entry) {
        super(entry);

        ChoiceBox<String> refs = new ChoiceBox<>();
        refs.getItems().setAll(getElements());

        refs.valueProperty().addListener((obs, oldval, newval) -> {
            getEntry().userInputValue(newval);
        });

        refs.getSelectionModel().select(RepoUtils.resToString(entry.getFieldValue()));

        setTitleContent(refs);

    }

    public Set<String> getElements() {
        IKind<?> ikind = (IKind<?>) getEntry().kind;
        String title = ikind.getTitle();
        Iterable<Resource<?>> ress = getEntry().getController().repository.getStagingTx().enumerate(title);
        Set<String> names = StreamSupport.stream(ress.spliterator(), false).map(RepoUtils::resToString)
                .collect(Collectors.toSet());
        if (getEntry().getFieldValue() == null) {
            names.add(null);
        }
        return names;
    }

}
