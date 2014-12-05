package net.objectof.actof.connectorui;


import net.objectof.actof.common.controller.change.Change;
import net.objectof.actof.connector.Connector;


public class ConnectionSelectedChange extends Change {

    Connector conn;

    public ConnectionSelectedChange(Connector conn) {
        this.conn = conn;
    }


    public Connector getConnector() {
        return conn;
    }


}
