package net.objectof.actof.repospy.resource;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.objectof.actof.common.component.editor.ResourceEditor;
import net.objectof.actof.common.component.resource.impl.AbstractResource;
import net.objectof.actof.common.util.ActofSerialize;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.connector.Connector;
import net.objectof.connector.Connectors;
import net.objectof.connector.Parameter;


public class RepositoryResource extends AbstractResource {

    Connector connector;

    @Override
    public ResourceEditor createEditor() throws Exception {
        RepoSpyController controller = new RepoSpyController();
        return controller;
    }

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
        setTitle(connector.getPackageName());
    }

    @Override
    public String toString() {
        return getTitle();
    }

    @Override
    public Map<String, Object> toSerializableForm() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", connector.getName());
        map.put("parameters", connector.getParameters());
        map.put("type", connector.getType());

        return map;
    }

    @Override
    public void fromSerializableForm(Map<String, Object> data) {
        String type = data.get("type").toString();
        String name = data.get("name").toString();
        List<Object> paramObjs = (List<Object>) data.get("parameters");

        connector = Connectors.getConnectorByType(type);
        connector.setName(name);
        for (Object o : paramObjs) {
            Parameter p = ActofSerialize.convertObject(o, Parameter.class);
            connector.setParameter(p.getTitle(), p.getValue());
        }
    }

}
