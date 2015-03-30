package net.objectof.actof.repospy.controllers.navigator.editor.layout;


import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import net.objectof.actof.common.icons.ActofIcons;
import net.objectof.actof.common.icons.ActofIcons.Icon;
import net.objectof.actof.common.icons.ActofIcons.Size;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.changes.EntityCreatedChange;
import net.objectof.actof.repospy.controllers.navigator.editor.cards.EntityCard;
import net.objectof.actof.repospy.controllers.navigator.treemodel.RepoSpyTreeItem;
import net.objectof.actof.repospy.controllers.navigator.treemodel.TreeNode;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.IAggregateNode;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.IKindNode;
import net.objectof.actof.widgets.card.Card;
import net.objectof.model.Resource;


public class KindLayout extends AbstractLayout {

    private IKindNode node;
    private Card controlCard = new Card();

    public KindLayout(RepoSpyTreeItem treeitem, RepoSpyController repospy) {
        super(treeitem, repospy, 350);
        this.node = (IKindNode) treeitem.getValue();
        this.treeitem = treeitem;

        Button add = new Button("", ActofIcons.getIconView(Icon.ADD, Size.BUTTON));
        add.getStyleClass().add("tool-bar-button");
        add.setTooltip(new Tooltip("Create Entity"));
        add.setOnAction(event -> {
            Resource<?> newEntity = repospy.repository.getStagingTx().create(node.getKind().getComponentName());
            EntityCreatedChange change = new EntityCreatedChange(newEntity);
            repospy.repository.addTransientEntity(newEntity);
            repospy.getChangeBus().broadcast(change);
            treeitem.getValue().refreshNode();
        });

        controlCard.setPadding(new Insets(0));
        controlCard.setRadius(0);
        controlCard.setTitle("Add Entity");
        controlCard.setDescription(add);
        controlCard.setHasShadow(true);
        controlCard.setColour("#ffffff");

        setTop(controlCard);

        updateUI();
    }

    @Override
    protected void updateUI() {

        cards.getChildren().clear();

        for (TreeItem<TreeNode> child : treeitem.getChildren()) {
            IAggregateNode aggChild = (IAggregateNode) (child.getValue());
            EntityCard card = new EntityCard((RepoSpyTreeItem) child, repospy);

            Node desc = card.getDescription();
            AnchorPane.setTopAnchor(desc, 0d);
            AnchorPane.setBottomAnchor(desc, 0d);
            AnchorPane.setLeftAnchor(desc, 0d);
            AnchorPane.setRightAnchor(desc, 0d);
            desc = new AnchorPane(desc);

            Button rem = new Button("", ActofIcons.getIconView(Icon.REMOVE, Size.BUTTON));
            rem.getStyleClass().add("tool-bar-button");
            rem.setTooltip(new Tooltip("Delete Entity"));
            rem.setOnAction(action -> {
                aggChild.delete();
                treeitem.updateChildren();
                updateUI();
            });
            HBox box = new HBox(10, desc, rem);
            card.setDescription(box);

            cards.getChildren().add(card);

        }
    }

}
