package net.objectof.actof.porter.visitor;


import net.objectof.model.Kind;
import net.objectof.model.Transaction;


public class IPorterContext {

    private Object key;
    private Object value;
    private Kind<?> kind;
    private Transaction fromTx;
    private Transaction toTx;

    private boolean dropped = false;

    public IPorterContext() {}

    public IPorterContext(Object key, Object value, Kind<?> kind) {
        this(key, value, kind, null, null);
    }

    public IPorterContext(Object key, Object value, Kind<?> kind, Transaction fromTx, Transaction toTx) {
        this.key = key;
        this.value = value;
        this.kind = kind;
        this.fromTx = fromTx;
        this.toTx = toTx;
    }

    public IPorterContext(IPorterContext copy) {
        key = copy.key;
        value = copy.value;
        kind = copy.kind;
        fromTx = copy.fromTx;
        toTx = copy.toTx;
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

    public Transaction getFromTx() {
        return fromTx;
    }

    public IPorterContext setFromTx(Transaction fromTx) {
        this.fromTx = fromTx;
        return this;
    }

    public Transaction getToTx() {
        return toTx;
    }

    public IPorterContext setToTx(Transaction toTx) {
        this.toTx = toTx;
        return this;
    }

    public boolean isDropped() {
        return dropped;
    }

    public void setDropped(boolean dropped) {
        this.dropped = dropped;
    }

    public String toString() {
        return "key = '" + key + "', value = '" + value + "', kind = " + kind + ", dropped = " + dropped;
    }

}
