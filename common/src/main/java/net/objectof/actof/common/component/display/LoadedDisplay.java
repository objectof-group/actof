package net.objectof.actof.common.component.display;


import javafx.scene.Node;


public interface LoadedDisplay extends Display {

    void setDisplayNode(Node node);

    /**
     * To be called after the display has been loaded and the node set.
     */
    default void onDisplayLoad() {};

}
