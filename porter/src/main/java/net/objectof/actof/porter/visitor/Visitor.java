package net.objectof.actof.porter.visitor;


import net.objectof.actof.porter.walker.Walker;
import net.objectof.model.Id;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Transaction;


public interface Visitor {

    void visit(Object key, Object value, Kind<?> kind, Id<?> parentId);

    Iterable<Resource<?>> getEntities(Kind<?> kind);

    public Walker getWalker();

    public void setWalker(Walker texasRanger);

    public abstract void setTx(Transaction tx);

    public abstract Transaction getTx();

}
