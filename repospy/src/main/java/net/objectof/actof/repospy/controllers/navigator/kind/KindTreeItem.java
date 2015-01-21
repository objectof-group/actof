package net.objectof.actof.repospy.controllers.navigator.kind;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import net.objectof.actof.common.controller.DynamicTreeItem;
import net.objectof.actof.common.controller.search.SearchController;
import net.objectof.actof.common.util.AlphaNumericComparitor;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.model.Resource;
import net.objectof.model.Transaction;

public class KindTreeItem extends DynamicTreeItem<KindTreeEntry> {

	RepoSpyController controller;
	
	public KindTreeItem(Resource<?> res, RepoSpyController controller) {
		this(new KindTreeEntry(res), controller);
	}
	
	public KindTreeItem(String entityKind, RepoSpyController controller) {
		this(new KindTreeEntry(entityKind), controller);
	}
	
	public KindTreeItem(KindTreeEntry kv, RepoSpyController controller) {
		super(kv);
		this.controller = controller;
	}

	@Override
	protected ObservableList<TreeItem<KindTreeEntry>> buildChildren(TreeItem<KindTreeEntry> TreeItem) {
		
		KindTreeEntry data = TreeItem.getValue();
		if (data.res != null) { return FXCollections.emptyObservableList(); }
		String kind = data.entityKind;
		
		//get transaction, and create result container
		Transaction tx = controller.repository.getStagingTx();
		ObservableList<TreeItem<KindTreeEntry>> newlist = FXCollections.observableArrayList();
		Iterable<Resource<?>> iter;
		
		SearchController search = controller.search;

		if (search.isValid() && kind.equals(search.getKind())) {
			iter = tx.query(kind, search.getQuery());
		} else if (search.isValid()) {
			return FXCollections.emptyObservableList();
		} else {
			iter = tx.enumerate(kind);
		}
		
		//persistent entities
		for (Resource<?> res : iter) {
			newlist.add(new TreeItem<KindTreeEntry>(new KindTreeEntry(res)));
		}
		
		//transient entities
		for (Resource<?> res : controller.repository.getTransientsForKind(kind)) {
			newlist.add(new TreeItem<KindTreeEntry>(new KindTreeEntry(res)));
		}
		
		//sort them all
		AlphaNumericComparitor comparitor = new AlphaNumericComparitor();
		newlist.sort((a, b) -> comparitor.compare(RepoUtils.resToString(a), RepoUtils.resToString(b)));
		
		return newlist;
		
	}

	@Override
	public boolean isLeafNode(KindTreeEntry t) {
		return t.res != null;
	}
	
}