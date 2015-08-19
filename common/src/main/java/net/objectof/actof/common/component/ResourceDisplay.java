package net.objectof.actof.common.component;


public interface ResourceDisplay extends Display {

    Resource getResource();

    void setResource(Resource resource);

    void loadResource() throws Exception;

}
