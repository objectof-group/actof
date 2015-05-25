package net.objectof.actof.repospy.migration;


import net.objectof.model.Kind;
import net.objectof.model.Transaction;


public class PorterContext {

    private Object key;
    private Object value;
    private Kind<?> kind;
    private Transaction fromTx;
    private Transaction toTx;

    public PorterContext() {}

    public PorterContext(Object key, Object value, Kind<?> kind, Transaction fromTx, Transaction toTx) {
        this.key = key;
        this.value = value;
        this.kind = kind;
        this.fromTx = fromTx;
        this.toTx = toTx;
    }

    public PorterContext(PorterContext copy) {
        key = copy.key;
        value = copy.value;
        kind = copy.kind;
        fromTx = copy.fromTx;
        toTx = copy.toTx;
    }

    public PorterContext copy() {
        return new PorterContext(this);
    }

    public Object getKey() {
        return key;
    }

    public PorterContext setKey(Object key) {
        this.key = key;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public PorterContext setValue(Object value) {
        this.value = value;
        return this;
    }

    public Kind<?> getKind() {
        return kind;
    }

    public PorterContext setKind(Kind<?> kind) {
        this.kind = kind;
        return this;
    }

    public Transaction getFromTx() {
        return fromTx;
    }

    public PorterContext setFromTx(Transaction fromTx) {
        this.fromTx = fromTx;
        return this;
    }

    public Transaction getToTx() {
        return toTx;
    }

    public PorterContext setToTx(Transaction toTx) {
        this.toTx = toTx;
        return this;
    }

    public String toString() {
        return "key = '" + key + "', value = '" + value + "', kind = " + kind;
    }

}
