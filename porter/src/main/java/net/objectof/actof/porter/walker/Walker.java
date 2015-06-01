package net.objectof.actof.porter.walker;


import net.objectof.actof.porter.visitor.Visitor;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Transaction;


public interface Walker {

    public abstract Visitor getVisitor();

    public abstract void setVisitor(Visitor visitor);

    public abstract Transaction getTx();

    public abstract void setTx(Transaction tx);

    /**
     * Walks the top-level entities (root nodes) in this forest (of trees),
     * visiting each element and its subtree.
     * 
     * @param kinds
     *            the {@link Kind}s of the top level entities
     */
    public abstract void walkEntities(Iterable<? extends Kind<?>> kinds);

    /**
     * Treats this object as if it were a {@link Resource} from the current
     * repository, and tries to walk it's subtree as best as possible. If the
     * resource is a scalar value (like an integer), no action is taken, as it
     * has no subtree to walk.
     * 
     * @param object
     *            the object to walk
     */
    public abstract void walk(Object object);

}