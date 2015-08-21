package net.objectof.actof.ide.resource;


import net.objectof.actof.common.component.editor.ResourceEditor;
import net.objectof.actof.common.component.resource.impl.AbstractResource;


public class ProjectResource extends AbstractResource {

    @Override
    public ResourceEditor createDisplay() throws Exception {
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
