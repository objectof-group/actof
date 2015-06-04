package net.objectof.actof.porter.visitor;


import net.objectof.model.Kind;
import net.objectof.model.Transaction;


public class IPorterContext {

    private Object key;
    private Object value;
    private Kind<?> kind;
    private Transaction tx;

    private boolean dropped = false;

    public IPorterContext() {}

    public IPorterContext(Object key, Object value, Kind<?> kind) {
        this(key, value, kind, null);
    }

    public IPorterContext(Object key, Object value, Kind<?> kind, Transaction tx) {
        this.key = key;
        this.value = value;
        this.kind = kind;
        this.tx = tx;
    }

    public IPorterContext(IPorterContext copy) {
        key = copy.key;
        value = copy.value;
        kind = copy.kind;
        tx = copy.tx;
    }

    public IPorterContext copy() {
        return new IPorterContext(this);
    }

    public Object getKey() {
        return key;
    }

    public IPorterContext setKey(Object key) {
        this.key = key;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public IPorterContext setValue(Object value) {
        this.value = value;
        return this;
    }

    public Kind<?> getKind() {
        return kind;
    }

    public IPorterContext setKind(Kind<?> kind) {
        this.kind = kind;
        return this;
    }

    public Transaction getTx() {
        return tx;
    }

    public IPorterContext setTx(Transaction tx) {
        this.tx = tx;
        return this;
    }

    public boolean isDropped() {
        return dropped;
    }

    public IPorterContext setDropped(boolean dropped) {
        this.dropped = dropped;
        return this;
    }

    public String toString() {
        return "key = '" + key + "', value = '" + value + "', kind = " + kind + ", dropped = " + dropped;
    }

}
