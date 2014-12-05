package net.objectof.actof.connector;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import net.objectof.actof.connector.parameter.Parameter;
import net.objectof.model.Package;



public abstract class AbstractConnector implements Connector {


    private String name = "";

    private List<Parameter> parameters = new ArrayList<>();

    protected void addParameter(Parameter param) {
        parameters.add(param);
    }

    protected void addParameter(Parameter.Type type, String title) {
        addParameter(type.create(title));
    }

    protected String value(String key) {
        return getParameter(key).getValue();
    }

    @Override
    public Package createPackage(InputStream schema) throws Exception {
        return createPackage(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(schema));
    }


    @Override
    public String toString() {
        return getName();
    }

    @Override
    public List<Parameter> getParameters() {
        return new ArrayList<>(parameters);
    }

    @Override
    public Parameter getParameter(String name) {
        for (Parameter p : parameters) {
            if (name.equals(p.getTitle())) { return p; }
        }
        return null;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (!Connector.class.isAssignableFrom(o.getClass())) { return false; }
        Connector other = (Connector) o;

        if (!other.getName().equals(getName())) { return false; }
        if (!other.getType().equals(getType())) { return false; }
        if (!other.getPackageName().equals(getPackageName())) { return false; }

        for (Parameter p : getParameters()) {
            Parameter op = other.getParameter(p.getTitle());
            if (!p.getValue().equals(op.getValue())) { return false; }
        }
        return true;
    }


}
