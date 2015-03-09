package net.objectof.actof.repospy.controllers.navigator.kind;


import javafx.beans.value.ObservableValueBase;
import net.objectof.actof.common.controller.change.Change;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.changes.FieldChange;
import net.objectof.aggr.Aggregate;
import net.objectof.model.Id;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;
import net.objectof.model.Transaction;


public class ILeafNode extends ObservableValueBase<ILeafNode> {

    private RepoSpyController repospy;

    public Id<?> parentId; // provided
    public Kind<?> kind; // provided - kind from the perspective of the parent,
                         // eg Person.job, rather than Job
    public Resource<?> parent; // calculated

    private Object value; // calculated
    public Object key; // provided

    public IResourceNode parentTreeEntry;
    public KindTreeItem treeNode;

    public ILeafNode(IResourceNode parentTreeEntry, RepoSpyController repospy, Kind<?> kind, Object key) {

        this.parentTreeEntry = parentTreeEntry;
        this.repospy = repospy;
        this.parentId = parentTreeEntry.getRes().id();
        this.kind = kind;
        this.key = key;

        refreshFromModel();
        repospy.getChangeBus().listen(this::onChange);

    }

    private void onChange(Change change) {

        change.when(FieldChange.class, fieldchange -> {
            if (getName().equals(fieldchange.getKey())) {
                refreshFromModel();
            }
        });

    }

    @Override
    public String toString() {
        if (value == null) { return "null"; }
        return RepoUtils.resToString(value);
    }

    public Object getKey() {
        return key;
    }

    public Stereotype getStereotype() {
        return kind.getStereotype();
    }

    public Object getFieldValue() {
        return value;
    }

    public String getName() {
        String record = RepoUtils.resToString(parent);
        Object field = key;
        if (field == null) { return record; }
        return record + "." + field;
    }

    public Object createFromNull() {
        Transaction tx = getController().repository.getStagingTx();
        Object newValue = tx.create(kind.getComponentName());
        writeToModel(newValue);
        return newValue;
    }

    public String title() {
        return RepoUtils.resToString(value);
    }

    @Override
    public ILeafNode getValue() {
        return this;
    }

    public RepoSpyController getController() {
        return repospy;
    }

    /**
     * Returns the kind of the resource from it's own perspective. If this
     * CompositeEntry was modeling Person.job, kind would be Person.job, whereas
     * this would return Job
     * 
     * @return null if this is not a Resource type, or if it is null, Kind of
     *         the resource otherwise TODO: Fix Me, currently checking the kind
     *         of the existing value, but when value is null, no way to
     *         determine this.
     */
    public Kind<?> resourceKind() {
        if (value == null) { return null; }
        if (!(value instanceof Resource)) { return null; }
        return ((Resource<?>) value).id().kind();
    }

    /**
     * Update the value of this field based on the given text
     */
    public void userInputValue(String text) {
        writeToModel(RepoUtils.valueFromString(kind, text, repospy.repository));
        addChangeHistory(text);
    }

    /**
     * Given the controller and the id of the parent resource, update the parent
     * resource and the field value.
     */
    public void refreshFromModel() {

        Transaction tx = repospy.repository.getStagingTx();
        parent = tx.retrieve(parentId);

        @SuppressWarnings("unchecked")
        Aggregate<Object, Object> agg = (Aggregate<Object, Object>) parent;
        value = agg.get(key);

        updateUI();

    }

    /**
     * Given a modification to the represented value, update the model (the
     * current staging transaction)
     */
    private void writeToModel(Object newValue) {

        Transaction tx = repospy.repository.getStagingTx();
        Resource<?> res = tx.retrieve(parentId);

        @SuppressWarnings("unchecked")
        Aggregate<Object, Object> agg = (Aggregate<Object, Object>) res;
        value = newValue;
        agg.set(key, value);

        addChangeHistory(newValue);

        updateUI();

    }

    private void updateUI() {
        // fire event indicating that this node has changed
        fireValueChangedEvent();
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

}
