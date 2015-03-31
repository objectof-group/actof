package net.objectof.actof.repospy.controllers.navigator.editor.layout;


import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import net.objectof.actof.common.icons.ActofIcons;
import net.objectof.actof.common.icons.ActofIcons.Icon;
import net.objectof.actof.common.icons.ActofIcons.Size;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf.LeafCard;
import net.objectof.actof.repospy.controllers.navigator.treemodel.RepoSpyTreeItem;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.IAggregateNode;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;
import net.objectof.actof.widgets.card.Card;


public abstract class AggregateLayout extends AbstractLayout {

    private Card controlCard = new Card();

    private IAggregateNode entry;

    public AggregateLayout(RepoSpyTreeItem treeitem, RepoSpyController repospy) {
        super(treeitem, repospy, 350);
        this.entry = (IAggregateNode) treeitem.getValue();

        Button add = new Button("", ActofIcons.getIconView(Icon.ADD, Size.BUTTON));
        add.setTooltip(new Tooltip("Add Entry"));
        add.getStyleClass().add("tool-bar-button");
        add.setOnAction(action -> {
            onAdd();
        });
        controlCard.setDescription(add);
        controlCard.setTitle("Add Entry");
        controlCard.setRadius(0);
        controlCard.setShadowRadius(8);
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

        for (ILeafNode leaf : entry.getLeaves()) {
            LeafCard editor = LeafCard.createEditor(leaf);

            Node desc = editor.getDescription();
            AnchorPane.setTopAnchor(desc, 0d);
            AnchorPane.setBottomAnchor(desc, 0d);
            AnchorPane.setLeftAnchor(desc, 0d);
            AnchorPane.setRightAnchor(desc, 0d);
            desc = new AnchorPane(desc);

            Button rem = new Button("", ActofIcons.getIconView(Icon.REMOVE, Size.BUTTON));
            rem.getStyleClass().add("tool-bar-button");
            rem.setTooltip(new Tooltip("Remove Entry"));
            rem.setOnAction(action -> {
                onRemove(leaf);
            });
            HBox box = new HBox(10, desc, rem);

            editor.setDescription(box);

            customizeCard(leaf, editor);
            cards.getChildren().add(editor);
        }
    }

    protected void customizeCard(ILeafNode node, LeafCard card) {}

    protected IAggregateNode getEntry() {
        return entry;
    }

}
