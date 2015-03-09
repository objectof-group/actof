package net.objectof.actof.repospy.controllers.navigator.composite;


import java.util.List;

import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.ResourceSelectedChange;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.Editor;
import net.objectof.actof.widgets.card.Card;
import net.objectof.actof.widgets.card.CardsPane;
import net.objectof.model.Resource;


public class CompositeView extends CardsPane {

    public CompositeView(ChangeController changes, RepoSpyController repospy) {
        changes.listen(ResourceSelectedChange.class, change -> {

            Resource<?> res = change.getEntry().getValue().getRes();

            getChildren().clear();

            List<CompositeEntry> entries = CompositeEntry.getSubEntries(res, repospy);

            for (CompositeEntry entry : entries) {
                Card card = new Card();
                Editor editor = Editor.createEditor(entry);
                card.setContent(editor.getNode(), editor.expand());
                String title = entry.getKey().toString();
                title = title.substring(0, 1).toUpperCase() + title.substring(1);
                card.setTitle(title);
                card.setDescription(entry.getStereotype().toString());

                getChildren().add(card);
            }

        });
    }
}
