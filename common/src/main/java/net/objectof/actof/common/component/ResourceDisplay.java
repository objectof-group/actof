package net.objectof.actof.common.component;


public interface ResourceDisplay extends Display {

    boolean isForResource();

    void setForResource(boolean forResource);

    Resource getResource();

    void setResource(Resource resource);

    void loadResource() throws Exception;

}
