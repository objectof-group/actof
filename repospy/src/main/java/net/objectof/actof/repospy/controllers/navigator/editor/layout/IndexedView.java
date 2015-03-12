package net.objectof.actof.repospy.controllers.navigator.editor.layout;


import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.treemodel.IAggregateNode;
import net.objectof.actof.repospy.controllers.navigator.treemodel.ILeafNode;
import net.objectof.aggr.Listing;
import net.objectof.model.Resource;


public class IndexedView extends AggregateView {

    public IndexedView(IAggregateNode entry, RepoSpyController repospy) {
        super(entry, repospy);
        updateUI();
    }

    @Override
    protected void onAdd() {
        Listing<?> list = (Listing<?>) entry.getRes();
        list.add(null);
        ILeafNode leaf = new ILeafNode(entry, repospy, entry.getRes().id().kind().getParts().get(0), list.size() - 1);
        leaf.addChangeHistory(null);
        entry.refreshLeaves(repospy);
        updateUI();
    }

    @Override
    protected void onRemove(ILeafNode leaf) {
        Listing<?> list = (Listing<?>) entry.getRes();
        Resource<?> subres = (Resource<?>) leaf.getFieldValue();
        list.remove(subres);
        leaf.addChangeHistory(null);
        entry.refreshLeaves(repospy);
        updateUI();
    }
}
