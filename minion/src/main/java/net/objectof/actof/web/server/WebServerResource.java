package net.objectof.actof.web.server;


import java.util.HashMap;
import java.util.Map;

import net.objectof.actof.common.component.editor.Editor;
import net.objectof.actof.common.component.resource.impl.AbstractResource;


public class WebServerResource extends AbstractResource {

    private WebServer server = new WebServer();

    public WebServerResource() {
        titleProperty().set("Minion Server");
    }

    @Override
    public Editor createEditor() throws Exception {
        return WebServerEditor.load();
    }

    public WebServer getServer() {
        return server;
    }

    public void setServer(WebServer server) {
        this.server = server;
    }

    @Override
    public Map<String, Object> toSerializableForm() {
        Map<String, Object> map = new HashMap<>();
        return map;
    }

    @Override
    public void fromSerializableForm(Map<String, Object> data) {}

}
