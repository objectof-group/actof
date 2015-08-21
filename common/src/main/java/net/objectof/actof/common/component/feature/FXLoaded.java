package net.objectof.actof.common.component.feature;


import javafx.scene.Node;


public interface FXLoaded {

    void setFXNode(Node node);

    /**
     * To be called after the display has been loaded and the node set.
     */
    default void onFXLoad() {};

}
