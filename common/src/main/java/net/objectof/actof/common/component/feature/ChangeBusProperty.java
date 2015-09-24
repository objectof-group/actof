package net.objectof.actof.common.component.feature;


import javafx.beans.property.ObjectProperty;
import net.objectof.actof.common.controller.change.ChangeController;


public interface ChangeBusProperty {

    ObjectProperty<ChangeController> changeBusProperty();

    default ChangeController getChangeBus() {
        return changeBusProperty().get();
    }

    default void setChangeBus(ChangeController changeBus) {
        changeBusProperty().set(changeBus);
    }

}
