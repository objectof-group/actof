package net.objectof.actof.connectorui;


import java.io.InputStream;
import java.util.List;

import net.objectof.actof.connector.Connector;
import net.objectof.actof.connector.parameter.Parameter;
import net.objectof.model.Package;

import org.w3c.dom.Document;


public class ConnectorUI implements Connector {

    private Connector backer;
    private String displayName;


    public ConnectorUI(Connector backer) {
        this.backer = backer;
        setDisplayName(backer.getName());
    }


    /**
     * Checks to see if the two connectorUIs are effectively equal. Disregards
     * name and displayname properties
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) { return false; }
        if (!Connector.class.isAssignableFrom(o.getClass())) { return false; }
        Connector other = (Connector) o;

        if (!other.getType().equals(getType())) { return false; }
        if (!other.getPackageName().equals(getPackageName())) { return false; }

        for (Parameter p : getParameters()) {
            Parameter op = other.getParameter(p.getTitle());
            if (!p.getValue().equals(op.getValue())) { return false; }
        }
        return true;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }




    @Override
    public Package getPackage() throws Exception {
        return backer.getPackage();
    }


    @Override
    public Package createPackage(Document schema) throws Exception {
        return backer.createPackage(schema);
    }


    @Override
    public Package createPackage(InputStream schema) throws Exception {
        return backer.createPackage(schema);
    }


    @Override
    public String getPackageName() {
        return backer.getPackageName();
    }


    @Override
    public List<Parameter> getParameters() {
        return backer.getParameters();
    }


    @Override
    public Parameter getParameter(String name) {
        return backer.getParameter(name);
    }


    @Override
    public String getName() {
        return backer.getName();
    }


    @Override
    public void setName(String name) {
        backer.setName(name);
    }


    @Override
    public String getType() {
        return backer.getType();
    }





}
