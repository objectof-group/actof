package net.objectof.actof.connectorui;


import java.io.InputStream;
import java.util.List;
import java.util.Map;

import net.objectof.connector.Connector;
import net.objectof.connector.ConnectorException;
import net.objectof.connector.parameter.Parameter;
import net.objectof.model.Package;

import org.w3c.dom.Document;


public class ConnectorUI implements Connector {

    private Connector backer;
    private String displayName;
    private boolean isTemplate = false;

    public ConnectorUI(Connector backer) {
        this.backer = backer;
        setDisplayName(backer.getName());
    }

    public boolean isTemplate() {
        return isTemplate;
    }

    public void setTemplate(boolean isTemplate) {
        this.isTemplate = isTemplate;
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
            if (p.getValue() == null) { return op.getValue() == null; }
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
    public Package getPackage() throws ConnectorException {
        return backer.getPackage();
    }

    @Override
    public Package createPackage(Document schema, Initialize initialize) throws ConnectorException {
        return backer.createPackage(schema, initialize);
    }

    @Override
    public Package createPackage(InputStream schema, Initialize initialize) throws ConnectorException {
        return backer.createPackage(schema, initialize);
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

    @Override
    public void setParameter(String parameter, String value) {
        Parameter p = getParameter(parameter);
        if (p == null) { return; }
        p.setValue(value);
    }

    @Override
    public void setParameters(Map<String, String> values) {
        for (String parameter : values.keySet()) {
            setParameter(parameter, values.get(parameter));
        }
    }

    @Override
    public void initializeDatabase() throws ConnectorException {
        backer.initializeDatabase();
    }

    @Override
    public boolean isDatabaseEmpty() throws ConnectorException {
        return backer.isDatabaseEmpty();
    }

    @Override
    public List<String> getSchemaNames() throws ConnectorException {
        return backer.getSchemaNames();
    }
}
