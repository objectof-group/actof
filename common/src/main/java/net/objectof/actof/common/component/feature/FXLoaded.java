package net.objectof.actof.common.component.feature;


import javafx.scene.layout.Region;


public interface FXLoaded {

    void setFXRegion(Region region);

    /**
     * To be called after the display has been loaded and the node set.
     */
    default void onFXLoad() {};

}
