package net.objectof.actof.widgets.network;


public class INetworkEdge implements NetworkEdge {

    private NetworkVertex from, to;

    public INetworkEdge(NetworkVertex from, NetworkVertex to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public NetworkVertex from() {
        return from;
    }

    @Override
    public NetworkVertex to() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NetworkEdge)) { return false; }
        NetworkEdge other = (NetworkEdge) o;
        return from.equals(other.from()) && to.equals(other.to());
    }

    @Override
    public int hashCode() {
        return from.hashCode() + to.hashCode();
    }
}
