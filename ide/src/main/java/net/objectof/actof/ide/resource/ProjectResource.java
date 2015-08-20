package net.objectof.actof.ide.resource;


import net.objectof.actof.common.component.ResourceDisplay;
import net.objectof.actof.common.component.impl.AbstractResource;


public class ProjectResource extends AbstractResource {

    @Override
    public ResourceDisplay createDisplay() throws Exception {
        return null;
    }

    @Override
    public String getTitle() {
        return "Project";
    }

    @Override
    public String toString() {
        return getTitle();
    }

}
