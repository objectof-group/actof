package net.objectof.actof.widgets.network;


import javafx.scene.Node;


public class INetworkEdge<T extends Node> implements NetworkEdge<T> {

    private T from, to;

    public INetworkEdge(T from, T to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public T from() {
        return from;
    }

    @Override
    public T to() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NetworkEdge)) { return false; }
        NetworkEdge<?> other = (NetworkEdge<?>) o;
        return from.equals(other.from()) && to.equals(other.to());
    }

    @Override
    public int hashCode() {
        return from.hashCode() + to.hashCode();
    }
}
