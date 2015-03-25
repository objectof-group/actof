package net.objectof.actof.repospy.controllers.navigator.treemodel.nodes;


import javafx.beans.value.ObservableValueBase;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.changes.EntityCreatedChange;
import net.objectof.actof.repospy.changes.FieldChange;
import net.objectof.actof.repospy.controllers.navigator.treemodel.RepoSpyTreeItem;
import net.objectof.aggr.Aggregate;
import net.objectof.model.Id;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;
import net.objectof.model.Transaction;


public class ILeafNode extends ObservableValueBase<ILeafNode> {

    private RepoSpyController repospy;

    private Id<?> parentId;
    private Kind<?> kind; // kind from the perspective of the parent. eg
                          // Person.job, rather than Job
    private Object key;
    private RepoSpyTreeItem treeNode;

    public ILeafNode(Id<?> parentId, RepoSpyController repospy, Kind<?> kind, Object key) {
        this.repospy = repospy;
        this.parentId = parentId;
        this.kind = kind;
        this.key = key;
        fireValueChangedEvent();
    }

    @Override
    public String toString() {
        return "LeafNode:" + getStereotype() + " = " + RepoUtils.resToString(getFieldValue());
    }

    public Object getKey() {
        return key;
    }

    public Stereotype getStereotype() {
        return kind.getStereotype();
    }

    public Object getFieldValue() {
        Resource<?> parent = getParent();
        @SuppressWarnings("unchecked")
        Aggregate<Object, Object> agg = (Aggregate<Object, Object>) parent;
        return agg.get(getKey());
    }

    public void setFieldValue(Object object) {
        Transaction tx = repospy.repository.getStagingTx();
        Resource<?> res = tx.retrieve(parentId);

        @SuppressWarnings("unchecked")
        Aggregate<Object, Object> agg = (Aggregate<Object, Object>) res;
        agg.set(key, object);

        addChangeHistory(object);
        fireValueChangedEvent();
    }

    public String getName() {
        String record = RepoUtils.resToString(getParent());
        Object field = key;
        if (field == null) { return record; }
        return record + "." + field;
    }

    public Object createFromNull() {
        Transaction tx = getController().repository.getStagingTx();
        Object newValue = tx.create(kind.getComponentName());
        repospy.getChangeBus().broadcast(new EntityCreatedChange((Resource<?>) newValue));
        setFieldValue(newValue);
        return newValue;
    }

    @Override
    public ILeafNode getValue() {
        return this;
    }

    public RepoSpyController getController() {
        return repospy;
    }

    public Kind<?> getKind() {
        return kind;
    }

    /**
     * Returns the kind of the resource from it's own perspective. If this node
     * represented Person.job, getKind would return Person.job, whereas this
     * method would return Job
     * 
     * @return null if this is not a Resource type, (or if the current value is
     *         null -- a known limitation), Kind of the resource otherwise TODO:
     *         Fix Me, currently checking the kind of the existing value, but
     *         when value is null, no way to determine this.
     */
    public Kind<?> getCanonicalKind() {
        Object value = getFieldValue();
        if (value == null) {
            // TODO: This is a bad solution, and will cause issues when a the
            // field value is null and the relative kind name doesn't match the
            // canonical kind name.
            return getKind();
        }
        if (!(value instanceof Resource)) { return null; }
        return ((Resource<?>) value).id().kind();
    }

    public void addChangeHistory(Object newValue) {
        FieldChange change = new FieldChange(getCleanValue(), newValue, this);
        repospy.getChangeBus().broadcast(change);
    }

    private Object getCleanValue() {
        Transaction cleanTx = repospy.repository.getCleanTx();
        Resource<?> cleanParent = cleanTx.retrieve(parentId);
        @SuppressWarnings("unchecked")
        Aggregate<Object, Object> agg = (Aggregate<Object, Object>) cleanParent;
        return agg.get(key);
    }

    public Resource<?> getParent() {
        Transaction tx = repospy.repository.getStagingTx();
        Resource<?> parent = tx.retrieve(parentId);
        return parent;
    }

    public RepoSpyTreeItem getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(RepoSpyTreeItem treeNode) {
        this.treeNode = treeNode;
    }

}
