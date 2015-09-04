package net.objectof.actof.common.component.feature;


import javafx.beans.property.ObjectProperty;
import net.objectof.actof.common.component.resource.Resource;


public interface ResourceProperty {

    ObjectProperty<Resource> resourceProperty();

    default void setResource(Resource resource) {
        resourceProperty().set(resource);
    }

    default Resource getResource() {
        return resourceProperty().get();
    }

}
