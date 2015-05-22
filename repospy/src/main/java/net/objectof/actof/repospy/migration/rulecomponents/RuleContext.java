package net.objectof.actof.repospy.migration.rulecomponents;


import net.objectof.model.Kind;
import net.objectof.model.Transaction;


public class RuleContext {

    private Object key;
    private Object value;
    private Kind<?> kind;
    private Transaction fromTx;
    private Transaction toTx;

    public RuleContext() {}

    public RuleContext(Object key, Object value, Kind<?> kind, Transaction fromTx, Transaction toTx) {
        this.key = key;
        this.value = value;
        this.kind = kind;
        this.fromTx = fromTx;
        this.toTx = toTx;
    }

    public RuleContext(RuleContext copy) {
        key = copy.key;
        value = copy.value;
        kind = copy.kind;
        fromTx = copy.fromTx;
        toTx = copy.toTx;
    }

    public RuleContext copy() {
        return new RuleContext(this);
    }

    public Object getKey() {
        return key;
    }

    public RuleContext setKey(Object key) {
        this.key = key;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public RuleContext setValue(Object value) {
        this.value = value;
        return this;
    }

    public Kind<?> getKind() {
        return kind;
    }

    public RuleContext setKind(Kind<?> kind) {
        this.kind = kind;
        return this;
    }

    public Transaction getFromTx() {
        return fromTx;
    }

    public RuleContext setFromTx(Transaction fromTx) {
        this.fromTx = fromTx;
        return this;
    }

    public Transaction getToTx() {
        return toTx;
    }

    public RuleContext setToTx(Transaction toTx) {
        this.toTx = toTx;
        return this;
    }

}
