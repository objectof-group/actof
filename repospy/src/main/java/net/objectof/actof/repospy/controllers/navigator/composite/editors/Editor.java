package net.objectof.actof.repospy.controllers.navigator.composite.editors;


import java.util.function.Consumer;

import javafx.scene.Node;


public interface Editor {

    void focus();

    /**
     * Callback for when editing is cancelled
     */
    void setOnCancel(Runnable onCancel);

    /**
     * Callback for when editing is complete
     */
    void setOnComplete(Consumer<String> onComplete);

    Node getNode();

    void modified();

    boolean isModified();

    default boolean expand() {
        return true;
    }

}
