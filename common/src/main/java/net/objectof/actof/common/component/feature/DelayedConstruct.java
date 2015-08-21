package net.objectof.actof.common.component.feature;


public interface DelayedConstruct {

    /**
     * To be called after properties have been set.
     * 
     * @throws Exception
     */
    void construct() throws Exception;

}
