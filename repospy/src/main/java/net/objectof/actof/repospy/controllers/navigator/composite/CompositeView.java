package net.objectof.actof.repospy.controllers.navigator.composite;


import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.ResourceSelectedChange;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.Editor;
import net.objectof.actof.repospy.controllers.navigator.kind.LeafEntry;
import net.objectof.actof.repospy.controllers.navigator.kind.ResourceTreeEntry;
import net.objectof.actof.widgets.card.Card;
import net.objectof.actof.widgets.card.CardsPane;


public class CompositeView extends CardsPane {

    public CompositeView(ChangeController changes, RepoSpyController repospy) {

        changes.listen(ResourceSelectedChange.class, change -> {
            getChildren().clear();
            if (!(change.getEntry().getValue() instanceof ResourceTreeEntry)) { return; }
            ResourceTreeEntry entry = (ResourceTreeEntry) change.getEntry().getValue();

            for (LeafEntry leaf : entry.getLeaves(repospy)) {
                Card card = new Card();
                Editor editor = Editor.createEditor(leaf);
                card.setContent(editor.getNode(), editor.expand());
                String title = leaf.getKey().toString();
                title = title.substring(0, 1).toUpperCase() + title.substring(1);
                card.setTitle(title);
                card.setDescription(leaf.getStereotype().toString());

                getChildren().add(card);
            }

        });
    }
}
