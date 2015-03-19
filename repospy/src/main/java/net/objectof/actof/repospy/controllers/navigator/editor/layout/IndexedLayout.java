package net.objectof.actof.repospy.controllers.navigator.editor.layout;


import javafx.scene.control.Labeled;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.common.util.RepoUtils.PrintStyle;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf.LeafCard;
import net.objectof.actof.repospy.controllers.navigator.treemodel.RepoSpyTreeItem;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;
import net.objectof.aggr.Listing;
import net.objectof.model.Resource;


public class IndexedLayout extends AggregateLayout {

    public IndexedLayout(RepoSpyTreeItem treeitem, RepoSpyController repospy) {
        super(treeitem, repospy);
        updateUI();
    }

    @Override
    protected void customizeCard(ILeafNode node, LeafCard card) {
        if (RepoUtils.isAggregateStereotype(node.getStereotype())) {
            card.setTitleContent(RepoUtils.prettyPrint(node.getFieldValue(), PrintStyle.LONG));
            Labeled title = (Labeled) card.getTitle();
            title.setText("#" + title.getText());
        }
    }

    @Override
    protected void onAdd() {
        Listing<?> list = (Listing<?>) getEntry().getRes();
        list.add(null);

        // TODO: There has to be a better way than creating a dummy leaf node
        // just to set the hisotry?
        ILeafNode leaf = new ILeafNode(getEntry().getRes().id(), repospy, getEntry().getRes().id().kind().getParts()
                .get(0), list.size() - 1);
        leaf.addChangeHistory(null);
        getEntry().refreshNode();
    }

    @Override
    protected void onRemove(ILeafNode leaf) {
        Listing<?> list = (Listing<?>) getEntry().getRes();
        Resource<?> subres = (Resource<?>) leaf.getFieldValue();
        list.remove(subres);
        leaf.addChangeHistory(null);
        getEntry().refreshNode();
    }
}
