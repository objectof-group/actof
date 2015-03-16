package net.objectof.actof.repospy.controllers.navigator.editor.layout;


import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
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
        super(treeitem, repospy);
        this.node = (IKindNode) treeitem.getValue();
        this.treeitem = treeitem;

        Button add = new Button("", ActofIcons.getIconView(Icon.ADD, Size.BUTTON));
        add.setOnAction(event -> {
            Resource<?> newEntity = repospy.repository.getStagingTx().create(node.getKind().getComponentName());
            EntityCreatedChange change = new EntityCreatedChange(newEntity);
            repospy.getChangeBus().broadcast(change);
            repospy.repository.addTransientEntity(newEntity);
            repospy.repository.getChangeBus().broadcast(new EntityCreatedChange(newEntity));
            treeitem.updateChildren();
            updateUI();
        });

        controlCard.setPadding(new Insets(0));
        controlCard.setRadius(0);
        controlCard.setTitle("Add Entity");
        controlCard.setDescription(add);

        setTop(controlCard);

        updateUI();
    }

    @Override
    protected void updateUI() {

        cards.getChildren().clear();

        for (TreeItem<TreeNode> child : treeitem.getChildren()) {
            IAggregateNode aggChild = (IAggregateNode) (child.getValue());
            EntityCard card = new EntityCard((RepoSpyTreeItem) child, repospy);
            cards.getChildren().add(card);
        }
    }
}
