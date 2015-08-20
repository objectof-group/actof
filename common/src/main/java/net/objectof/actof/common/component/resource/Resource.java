package net.objectof.actof.common.component.resource;

import net.objectof.actof.common.component.display.ResourceDisplay;

public interface Resource {

    ResourceDisplay getDisplay() throws Exception;

    ResourceDisplay createDisplay() throws Exception;

    String getTitle();

}
