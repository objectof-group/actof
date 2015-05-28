package net.objectof.actof.porter.visitor;


import net.objectof.actof.porter.Porter;
import net.objectof.actof.porter.PorterContext;
import net.objectof.aggr.Aggregate;
import net.objectof.model.Id;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;
import net.objectof.model.Transaction;


/**
 * Visitor for migrating old repo tree to new repo tree
 * 
 * @author NAS
 *
 */
public class MigrationVisitor implements Visitor {

    public PorterContext visitContainer(Porter porter, PorterContext context,
            Resource<Aggregate<Object, Object>> toParent) {

        PorterContext ported = porter.transform(context);
        if (ported.isDropped()) { return ported; }

        if (ported.getKind().getStereotype() == Stereotype.REF) {
            // there's a chance that the user passed us a reference to something
            // in the old repo.
            porter.runLater(() -> {
                if (toParent == null) { return; }
                toParent.value().set(porter.unqualify(ported.getKey(), toParent), ported.getValue());
            });
        } else {
            if (toParent == null) { return ported; }
            toParent.value().set(porter.unqualify(ported.getKey(), toParent), ported.getValue());
        }

        return ported;

    }

    public PorterContext visitLeaf(Porter porter, PorterContext context, Resource<Aggregate<Object, Object>> toParent) {
        PorterContext ported = porter.transform(context);
        if (ported.isDropped()) { return ported; }
        toParent.value().set(porter.unqualify(ported.getKey(), toParent), ported.getValue());
        return ported;
    }

    @Override
    public void onCreate(String kind, Resource<?> res) {
        // TODO Auto-generated method stub

    }

    @Override
    public Iterable<Resource<?>> getEntities(Kind<?> kind, Transaction fromTx, Transaction toTx) {
        return fromTx.enumerate(kind.getComponentName());
    }

    @Override
    public Iterable<Kind<?>> getCompositeParts(Id<?> from, Id<?> to) {
        return (Iterable<Kind<?>>) from.kind().getParts();
    }

    @Override
    public Iterable<Object> getAggregateParts(Resource<Aggregate<Object, Object>> from,
            Resource<Aggregate<Object, Object>> to) {
        return from.value().keySet();
    }

    @Override
    public Kind<?> getAggregateKind(Id<?> from, Id<?> to) {
        return from.kind().getParts().get(0);
    }

}
