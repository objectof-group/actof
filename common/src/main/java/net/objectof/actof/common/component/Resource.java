package net.objectof.actof.common.component;


public interface Resource {

    ResourceDisplay getDisplay() throws Exception;

    ResourceDisplay createDisplay() throws Exception;

    String getTitle();

}
