package net.objectof.actof.repospy.controllers.navigator.composite;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.scene.control.TreeItem;
import net.objectof.actof.common.controller.DynamicTreeItem;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.aggr.Aggregate;
import net.objectof.model.Id;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;
import net.objectof.model.Transaction;
import net.objectof.model.impl.IKind;

public class CompositeTreeItem extends DynamicTreeItem<CompositeEntry> {

	RepoSpyController controller;
	
	public CompositeTreeItem(CompositeEntry entry, RepoSpyController controller) {
		super(entry);
		entry.setEntryItem(this);
		this.controller = controller;
	}

	@Override
	protected List<TreeItem<CompositeEntry>> buildChildren(TreeItem<CompositeEntry> TreeItem) {

		CompositeEntry f = TreeItem.getValue();

		Id<?> id = RepoUtils.getIdOrNull(f.getFieldValue());
		if (id == null) { return new ArrayList<>(); }

		Transaction tx = controller.repository.getStagingTx();
		Resource<?> res = tx.retrieve(id);

		if (res.id().kind().getStereotype() == Stereotype.COMPOSED) {
			return buildForComposite(res);
		} else {
			return buildForAggredate(res);
		}
		
	}


	protected  List<TreeItem<CompositeEntry>> buildForAggredate(Resource<?> res) {
		
		@SuppressWarnings("unchecked")
		Aggregate<?, Resource<?>> agg = (Aggregate<?, Resource<?>>) res;
		Set<?> keys = agg.keySet();
		List<TreeItem<CompositeEntry>> children = new ArrayList<>();
		CompositeTreeItem node;
		
		Kind<?> kind = res.id().kind().getParts().get(0);
		
		for (Object key : keys) {
			CompositeEntry entry = new CompositeEntry(controller, res.id(), kind, key);
			node = new CompositeTreeItem(entry, controller);
			children.add(node);
		}

		return children;
		
	}
	
	
	protected List<TreeItem<CompositeEntry>> buildForComposite(Resource<?> res) {
		
		List<TreeItem<CompositeEntry>> children = new ArrayList<>();
		CompositeTreeItem node;
		
		for (Kind<?> kind : res.id().kind().getParts()) {
			IKind<?> ikind = (IKind<?>) kind;
			Object key = ikind.getSelector();
			CompositeEntry entry = new CompositeEntry(controller, res.id(), kind, key);
			node = new CompositeTreeItem(entry, controller);
			children.add(node);
		}

		return children;
		
	}
	

	@Override
	public boolean isLeafNode(CompositeEntry kv) {
		return !RepoUtils.isAggregate(kv.getFieldValue());
	}


}
