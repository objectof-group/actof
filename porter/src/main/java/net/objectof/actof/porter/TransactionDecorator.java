package net.objectof.actof.porter;


import java.io.Reader;

import net.objectof.model.Id;
import net.objectof.model.Package;
import net.objectof.model.Resource;
import net.objectof.model.Transaction;
import net.objectof.model.query.Query;


public class TransactionDecorator implements Transaction {

    Porter porter;
    Transaction tx;

    public TransactionDecorator(Porter porter, Transaction tx) {
        this.porter = porter;
        this.tx = tx;
        if (tx instanceof TransactionDecorator) {
            tx = ((TransactionDecorator) tx).tx;
        }
    }

    public void close() {
        tx.close();
    }

    public <T> T create(String aKind) {
        System.out.println("Decorated Transaction creating " + aKind);
        T t = tx.create(aKind);
        porter.addTransient(aKind, (Resource<?>) t);
        return t;
    }

    public <T> Iterable<T> enumerate(String kind) throws UnsupportedOperationException {
        return tx.enumerate(kind);
    }

    public Package getPackage() {
        return tx.getPackage();
    }

    public Status getStatus() {
        return tx.getStatus();
    }

    public void post() throws IllegalStateException {
        tx.post();
    }

    public <T> Iterable<T> query(String kind, Query query) throws IllegalArgumentException,
            UnsupportedOperationException {
        return tx.query(kind, query);
    }

    public <T> Iterable<T> query(String kind, String query) throws IllegalArgumentException,
            UnsupportedOperationException {
        return tx.query(kind, query);
    }

    public <T> T receive(String aMediaType, Reader aReader) {
        return tx.receive(aMediaType, aReader);
    }

    public <T> T retrieve(String aKind, Object aLabel) {
        return tx.retrieve(aKind, aLabel);
    }

    public <T> T retrieve(Id<?> aId) {
        return tx.retrieve(aId);
    }

    public void setLimit(int aLimit) {
        tx.setLimit(aLimit);
    }

}
