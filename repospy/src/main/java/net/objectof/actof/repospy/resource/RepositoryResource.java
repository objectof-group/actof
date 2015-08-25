package net.objectof.actof.repospy.resource;


import net.objectof.actof.common.component.editor.ResourceEditor;
import net.objectof.actof.common.component.resource.impl.AbstractResource;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.connector.Connector;


public class RepositoryResource extends AbstractResource {

    Connector connector;

    @Override
    public ResourceEditor createEditor() throws Exception {
        return new RepoSpyController();
    }

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    @Override
    public String getTitle() {
        return connector.getPackageName();
    }

    @Override
    public String toString() {
        return getTitle();
    }

}
