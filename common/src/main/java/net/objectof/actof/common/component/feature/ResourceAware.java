package net.objectof.actof.common.component.feature;


import net.objectof.actof.common.component.resource.Resource;


public interface ResourceAware {

    boolean isForResource();

    void setForResource(boolean forResource);

    Resource getResource();

    void setResource(Resource resource);

    void loadResource() throws Exception;

}
