package net.objectof.actof.common.component.display;

import net.objectof.actof.common.component.resource.Resource;

public interface ResourceDisplay extends Display {

    boolean isForResource();

    void setForResource(boolean forResource);

    Resource getResource();

    void setResource(Resource resource);

    void loadResource() throws Exception;

}
