package net.objectof.actof.common.component.resource;


import java.util.Optional;

import javafx.beans.property.BooleanProperty;
import net.objectof.actof.common.component.feature.Titled;


public interface Action extends Titled {

    default boolean isEnabled() {
        return getEnabledProperty().get();
    }

    default void setEnabled(boolean enabled) {
        getEnabledProperty().set(enabled);
    }

    BooleanProperty getEnabledProperty();

    Optional<Resource> perform();

}
