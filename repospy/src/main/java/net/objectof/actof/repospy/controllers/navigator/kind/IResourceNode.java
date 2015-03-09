package net.objectof.actof.repospy.controllers.navigator.kind;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.aggr.Aggregate;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;
import net.objectof.model.impl.IKind;
import net.objectof.model.impl.aggr.IComposite;


public class IResourceNode implements TreeNode {

    private Resource<?> res;

    private List<ILeafNode> leaves;
    private List<KindTreeItem> subresources;

    public IResourceNode(Resource<?> res) {
        this.res = res;

    }

    @Override
    public String toString() {

        String name = res.id().kind().getComponentName();
        if (res instanceof IComposite) {
            return name + " #" + res.id().label().toString();
        } else {
            String[] parts = name.split("\\.");
            name = parts[parts.length - 1];
            return name;
        }
    }

    public Resource<?> getRes() {
        return res;
    }

    public String getEntityKind() {
        throw new UnsupportedOperationException();
    }

    public boolean hasChildren() {
        for (Kind<?> kind : res.id().kind().getParts()) {
            switch (kind.getStereotype()) {

                case COMPOSED:
                case MAPPED:
                case INDEXED:
                case SET:
                    return true;
                default:
                    break;

            }
        }
        return false;
    }

    @Override
    public List<KindTreeItem> getChildren(RepoSpyController repospy) {

        if (subresources == null) {
            getLeafEntries(this, repospy);
        }

        /*
         * 
         * List<ResourceTreeEntry> children = new ArrayList<>(); IComposite
         * composite = (IComposite) res;
         * 
         * for (Kind<?> kind : res.id().kind().getParts()) {
         * 
         * System.out.println(kind); switch (kind.getStereotype()) {
         * 
         * case COMPOSED: break; case SET: case INDEXED: case MAPPED: String[]
         * keyParts = kind.getComponentName().split("\\."); String key =
         * keyParts[keyParts.length - 1]; Resource<?> map = (Resource<?>)
         * composite.get(key); children.add(new ResourceTreeEntry(map));
         * 
         * default: break;
         * 
         * } }
         * 
         * return children;
         */

        return subresources;

    }

    public List<ILeafNode> getLeaves(RepoSpyController repospy) {

        if (leaves == null) {
            getLeafEntries(this, repospy);
        }

        return leaves;
    }

    @Override
    public Stereotype getStereotype() {
        return res.id().kind().getStereotype();
    }

    public static void getLeafEntries(IResourceNode parent, RepoSpyController controller) {
        if (parent.getStereotype() == Stereotype.COMPOSED) {
            leafEntriesForComposite(parent, controller);
        } else {
            leafEntriesForAggredate(parent, controller);
        }

    }

    private static void leafEntriesForAggredate(IResourceNode parent, RepoSpyController controller) {

        @SuppressWarnings("unchecked")
        Aggregate<?, Resource<?>> agg = (Aggregate<?, Resource<?>>) parent.getRes();
        Set<?> keys = agg.keySet();

        Kind<?> kind = parent.getRes().id().kind().getParts().get(0);

        parent.leaves = new ArrayList<>();
        parent.subresources = new ArrayList<>();
        for (Object key : keys) {
            ILeafNode entry = new ILeafNode(parent, controller, kind, key);
            if (entry.getFieldValue() == null) {
                entry.createFromNull();
            }

            if (RepoUtils.isAggregateStereotype(entry.getStereotype())) {
                KindTreeItem subentry = new KindTreeItem(new IResourceNode((Resource<?>) entry.getFieldValue()),
                        controller);
                parent.subresources.add(subentry);
                entry.treeNode = subentry;
            }

            parent.leaves.add(entry);
        }

    }

    private static void leafEntriesForComposite(IResourceNode parent, RepoSpyController controller) {

        parent.leaves = new ArrayList<>();
        parent.subresources = new ArrayList<>();
        for (Kind<?> kind : parent.getRes().id().kind().getParts()) {
            IKind<?> ikind = (IKind<?>) kind;
            Object key = ikind.getSelector();
            ILeafNode entry = new ILeafNode(parent, controller, kind, key);
            if (entry.getFieldValue() == null) {
                entry.createFromNull();
            }

            if (RepoUtils.isAggregateStereotype(entry.getStereotype())) {
                KindTreeItem subentry = new KindTreeItem(new IResourceNode((Resource<?>) entry.getFieldValue()),
                        controller);
                parent.subresources.add(subentry);
                entry.treeNode = subentry;
            }

            parent.leaves.add(entry);
        }

    }

}
