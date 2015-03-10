package net.objectof.actof.repospy.controllers.navigator.editor.layout;


import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.editor.cards.LeafCard;
import net.objectof.actof.repospy.controllers.navigator.treemodel.IAggregateNode;
import net.objectof.actof.repospy.controllers.navigator.treemodel.ILeafNode;
import net.objectof.actof.widgets.card.Card;
import net.objectof.actof.widgets.card.CardsPane;
import net.objectof.aggr.Listing;
import net.objectof.model.Resource;


public class IndexedView extends CardsPane {

    public IndexedView(IAggregateNode entry, RepoSpyController repospy) {
        updateUI(entry, repospy);
    }

    private void updateUI(IAggregateNode entry, RepoSpyController repospy) {
        getChildren().clear();

        Image addimg = new Image(IndexedView.class.getResourceAsStream("icons/add.png"));
        Image remimg = new Image(IndexedView.class.getResourceAsStream("icons/remove.png"));

        Button add = new Button("", new ImageView(addimg));
        add.setOnAction(action -> {
            Listing<?> list = (Listing<?>) entry.getRes();
            list.add(null);

            ILeafNode leaf = new ILeafNode(entry, repospy, entry.getRes().id().kind().getParts().get(0),
                    list.size() - 1);
            leaf.addChangeHistory(null);

            entry.refreshLeaves(repospy);
            updateUI(entry, repospy);
        });

        Card newitem = new Card();
        newitem.setTitle("Add Entry");
        newitem.setDescription(add);
        getChildren().add(newitem);

        for (ILeafNode leaf : entry.getLeaves(repospy)) {
            LeafCard editor = LeafCard.createEditor(leaf);

            Node desc = editor.getDescription();
            AnchorPane.setTopAnchor(desc, 0d);
            AnchorPane.setBottomAnchor(desc, 0d);
            AnchorPane.setLeftAnchor(desc, 0d);
            AnchorPane.setRightAnchor(desc, 0d);
            desc = new AnchorPane(desc);

            Button rem = new Button("", new ImageView(remimg));
            rem.setOnAction(action -> {
                Listing<?> list = (Listing<?>) entry.getRes();
                Resource<?> subres = (Resource<?>) leaf.getFieldValue();
                list.remove(subres);
                leaf.addChangeHistory(null);
                entry.refreshLeaves(repospy);
                updateUI(entry, repospy);
            });
            HBox box = new HBox(10, desc, rem);

            editor.setDescription(box);

            getChildren().add(editor);
        }
    }
}
