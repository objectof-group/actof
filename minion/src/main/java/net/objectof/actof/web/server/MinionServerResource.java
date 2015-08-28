package net.objectof.actof.web.server;


import java.util.HashMap;
import java.util.Map;

import net.objectof.actof.common.component.editor.ResourceEditor;
import net.objectof.actof.common.component.resource.impl.AbstractResource;


public class MinionServerResource extends AbstractResource {

    private MinionServer server = new MinionServer();

    public MinionServerResource() {
        title = "Minion Server";
    }

    @Override
    public ResourceEditor createEditor() throws Exception {
        return MinionServerEditor.load();
    }

    public MinionServer getServer() {
        return server;
    }

    public void setServer(MinionServer server) {
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
