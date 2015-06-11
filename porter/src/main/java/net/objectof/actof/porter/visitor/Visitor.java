package net.objectof.actof.porter.visitor;


import net.objectof.actof.porter.walker.Walker;
import net.objectof.model.Id;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Transaction;


public interface Visitor {

    void visit(Object key, Object value, Kind<?> kind, Id<?> parentId);

    Iterable<Resource<?>> getEntities(Kind<?> kind);

    Walker getWalker();

    void setWalker(Walker texasRanger);

    void setTx(Transaction tx);

    Transaction getTx();

}
