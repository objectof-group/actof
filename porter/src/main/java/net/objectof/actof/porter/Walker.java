package net.objectof.actof.porter;


import net.objectof.actof.porter.visitor.Visitor;
import net.objectof.aggr.Aggregate;
import net.objectof.model.Id;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;
import net.objectof.model.Transaction;


public class Walker {

    private Visitor visitor;
    private Transaction tx;

    public Walker(Transaction tx) {
        this.tx = tx;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public Transaction getTx() {
        return tx;
    }

    public void setTx(Transaction tx) {
        this.tx = tx;
    }

    public void walkEntities(Iterable<? extends Kind<?>> kinds) {

        for (Kind<?> kind : kinds) {

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

    /**
     * Ports the elements of a container
     * 
     * @param id
     *            the Id of the container resource in the source repo
     */
    public void walkContainer(Id<?> id) {

        System.out.println("Walking " + id);

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

    public void walkComposite(Id<?> id) {

        System.out.println("Walking as Composite");

        Resource<Aggregate<Object, Object>> composite = tx.retrieve(id);

        // composed entities have different kinds for each field
        for (Kind<?> childKind : visitor.getCompositeParts(id)) {
            Object key = childKind.getComponentName();
            Object value = composite.value().get(PorterUtil.unqualify(key, composite));
            visitor.visit(key, value, childKind, id);
        }
    }

    public void walkAggregate(Id<?> id) {

        System.out.println("Walking as Aggregate");

        // this aggregate doesn't exist in new repo
        // if (!porter.getIdmap().containsKey(id)) { return; }

        Resource<Aggregate<Object, Object>> aggr = tx.retrieve(id);

        // aggregates (non-composed) only have 1 part describing the contents
        Kind<?> childKind = visitor.getAggregateKind(id);

        for (Object key : visitor.getAggregateParts(aggr)) {
            Object value = aggr.value().get(key);
            visitor.visit(key, value, childKind, id);
        }
    }

}
