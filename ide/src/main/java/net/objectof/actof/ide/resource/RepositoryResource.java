package net.objectof.actof.ide.resource;


import net.objectof.actof.common.component.Display;
import net.objectof.actof.common.component.Resource;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.connector.Connector;


public class RepositoryResource implements Resource {

    Connector connector;

    @Override
    public Display getDisplay() throws Exception {
        Display display = new RepoSpyController();
        display.setResource(this);
        return display;
    }

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

}
