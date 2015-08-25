package net.objectof.actof.minion.components.server;


import net.objectof.actof.common.component.editor.ResourceEditor;
import net.objectof.actof.common.component.resource.impl.AbstractResource;


public class MinionServerResource extends AbstractResource {

    private MinionServer server = new MinionServer();

    @Override
    public String getTitle() {
        return "Minion Server";
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

}
