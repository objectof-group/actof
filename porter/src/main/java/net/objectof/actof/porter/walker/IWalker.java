package net.objectof.actof.porter.walker;


import net.objectof.actof.porter.IPorterUtil;
import net.objectof.actof.porter.visitor.Visitor;
import net.objectof.aggr.Aggregate;
import net.objectof.model.Id;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;
import net.objectof.model.Transaction;


public class IWalker implements Walker {

    private Visitor visitor;
    private Transaction tx;

    public IWalker(Transaction tx) {
        this.tx = tx;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.objectof.actof.porter.Walker#getVisitor()
     */
    @Override
    public Visitor getVisitor() {
        return visitor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.objectof.actof.porter.Walker#setVisitor(net.objectof.actof.porter
     * .visitor.Visitor)
     */
    @Override
    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.objectof.actof.porter.Walker#getTx()
     */
    @Override
    public Transaction getTx() {
        return tx;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.objectof.actof.porter.Walker#setTx(net.objectof.model.Transaction)
     */
    @Override
    public void setTx(Transaction tx) {
        this.tx = tx;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.objectof.actof.porter.Walker#walkEntities(java.lang.Iterable)
     */
    @Override
    public void walk() {

        for (Kind<?> kind : tx.getPackage().getParts()) {

            // skip non-entities
            if (kind.getPartOf() != null) {
                continue;
            }

            Iterable<Resource<?>> resources = visitor.getEntities(kind);
            for (Resource<?> resource : resources) {
                Object key = kind.getComponentName();
                visitor.visit(key, resource, resource.id().kind(), null);
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see net.objectof.actof.porter.Walker#walk(java.lang.Object)
     */
    @Override
    public void walk(Object object) {
        if (!(object instanceof Resource)) { return; }
        Resource<?> res = (Resource<?>) object;

        try {
            walkContainer(res.id());
        }
        catch (UnsupportedOperationException e) {
            // Do nothing
        }
    }

    /**
     * Ports the elements of a container
     * 
     * @param id
     *            the Id of the container resource in the source repo
     */
    private void walkContainer(Id<?> id) {

        // System.out.println("Walking " + id);

        Stereotype st = id.kind().getStereotype();
        switch (st) {
            case COMPOSED:
                walkComposite(id);
                break;
            case INDEXED:
            case MAPPED:
            case SET:
                walkAggregate(id);
                break;
            default:
                throw new UnsupportedOperationException();
        }

    }

    private void walkComposite(Id<?> id) {

        // System.out.println("Walking as Composite");

        Resource<Aggregate<Object, Object>> composite = tx.retrieve(id);

        // composed entities have different kinds for each field
        for (Kind<?> childKind : id.kind().getParts()) {
            Object key = childKind.getComponentName();
            Object value = composite.value().get(IPorterUtil.unqualify(key, composite));
            visitor.visit(key, value, childKind, id);
        }
    }

    private void walkAggregate(Id<?> id) {

        // System.out.println("Walking as Aggregate");

        // this aggregate doesn't exist in new repo
        // if (!porter.getIdmap().containsKey(id)) { return; }

        Resource<Aggregate<Object, Object>> aggr = tx.retrieve(id);

        // aggregates (non-composed) only have 1 part describing the contents
        Kind<?> childKind = id.kind().getParts().get(0);

        for (Object key : aggr.value().keySet()) {
            Object value = aggr.value().get(key);
            visitor.visit(key, value, childKind, id);
        }
    }

}
