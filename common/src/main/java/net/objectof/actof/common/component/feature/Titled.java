package net.objectof.actof.common.component.feature;


import javafx.beans.property.StringProperty;


public interface Titled {

    StringProperty titleProperty();

    default String getTitle() {
        return titleProperty().get();
    }

    default void setTitle(String title) {
        titleProperty().set(title);
    }

}
