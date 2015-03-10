package net.objectof.actof.repospy.controllers.navigator.editor.layout;


import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.editor.cards.LeafCard;
import net.objectof.actof.repospy.controllers.navigator.treemodel.ILeafNode;
import net.objectof.actof.repospy.controllers.navigator.treemodel.IResourceNode;
import net.objectof.actof.widgets.card.Card;
import net.objectof.actof.widgets.card.CardsPane;


public class IndexedView extends CardsPane {

    public IndexedView(IResourceNode entry, RepoSpyController repospy) {

        getChildren().clear();

        ImageView addimg = new ImageView(new Image(IndexedView.class.getResourceAsStream("icons/add.png")));
        Button add = new Button("", addimg);

        Card newitem = new Card();
        newitem.setTitle("Add Entry");
        newitem.setDescription(add);
        getChildren().add(newitem);

        for (ILeafNode leaf : entry.getLeaves(repospy)) {
            LeafCard editor = LeafCard.createEditor(leaf);
            getChildren().add(editor);
        }

    }

}
