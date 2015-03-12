package net.objectof.actof.repospy.controllers.navigator.editor.layout;


import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf.LeafCard;
import net.objectof.actof.repospy.controllers.navigator.treemodel.RepoSpyTreeItem;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.IAggregateNode;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;
import net.objectof.actof.widgets.card.Card;


public abstract class AggregateView extends AbstractView {

    private Card controlCard = new Card();

    protected Image addimg = new Image(IndexedView.class.getResourceAsStream("../icons/add.png"));
    protected Image remimg = new Image(IndexedView.class.getResourceAsStream("../icons/remove.png"));
    protected boolean capitalize = true;
    protected IAggregateNode entry;

    public AggregateView(RepoSpyTreeItem treeitem, RepoSpyController repospy) {
        super(treeitem, repospy);
        this.entry = (IAggregateNode) treeitem.getValue();

        Button add = new Button("", new ImageView(addimg));
        add.setOnAction(action -> {
            onAdd();
        });
        controlCard.setDescription(add);
        controlCard.setTitle("Add Entry");
        controlCard.setRadius(0);
        controlCard.setPadding(new Insets(0));
        setTop(controlCard);
    }

    protected abstract void onAdd();

    protected abstract void onRemove(ILeafNode leaf);

    protected Card getControlCard() {
        return controlCard;
    }

    @Override
    protected void updateUI() {

        cards.getChildren().clear();

        for (ILeafNode leaf : entry.getLeaves(repospy)) {
            LeafCard editor = LeafCard.createEditor(leaf, capitalize);

            Node desc = editor.getDescription();
            AnchorPane.setTopAnchor(desc, 0d);
            AnchorPane.setBottomAnchor(desc, 0d);
            AnchorPane.setLeftAnchor(desc, 0d);
            AnchorPane.setRightAnchor(desc, 0d);
            desc = new AnchorPane(desc);

            Button rem = new Button("", new ImageView(remimg));
            rem.setOnAction(action -> {
                onRemove(leaf);
            });
            HBox box = new HBox(10, desc, rem);

            editor.setDescription(box);

            cards.getChildren().add(editor);
        }
    }

}
