package net.objectof.actof.repospy.controllers.navigator.editor.layout;


import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.editor.cards.LeafCard;
import net.objectof.actof.repospy.controllers.navigator.treemodel.IAggregateNode;
import net.objectof.actof.repospy.controllers.navigator.treemodel.ILeafNode;
import net.objectof.actof.widgets.card.CardsPane;


public class CompositeView extends CardsPane {

    public CompositeView(IAggregateNode entry, RepoSpyController repospy) {

        getChildren().clear();

        for (ILeafNode leaf : entry.getLeaves(repospy)) {
            LeafCard editor = LeafCard.createEditor(leaf);
            getChildren().add(editor);
        }

    }
}
