package net.objectof.actof.common.component.resource;


import javafx.beans.property.BooleanProperty;
import net.objectof.actof.common.component.feature.Titled;


public interface Action extends Titled, Runnable {

    default boolean isEnabled() {
        return getEnabledProperty().get();
    }

    default void setEnabled(boolean enabled) {
        getEnabledProperty().set(enabled);
    }

    BooleanProperty getEnabledProperty();

}
