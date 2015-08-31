package net.objectof.actof.common.component.feature;


import javafx.beans.property.BooleanProperty;


public interface Dismissible {

    default void dismiss() {
        if (!isDismissible()) { return; }
        dismissedProperty().set(true);
    }

    BooleanProperty dismissibleProperty();

    default boolean isDismissible() {
        return dismissibleProperty().get();
    }

    default void setDismissible(boolean dismissible) {
        dismissibleProperty().set(dismissible);
    }

    BooleanProperty dismissedProperty();

    default boolean getDismissed() {
        return dismissedProperty().get();
    }

}
