package net.objectof.actof.minion.classpath;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import net.objectof.actof.minion.components.handlers.style.HandlerCategory;


public class MinionHandler {

    StringProperty title;
    Class<?> handlerClass;
    HandlerCategory category;

    public MinionHandler(Class<?> cls, HandlerCategory category) {
        this.handlerClass = cls;
        this.category = category;
        title = new SimpleStringProperty(cls.getSimpleName());
    }

    public MinionHandler(MinionHandler other) {
        this.title = other.title;
        this.handlerClass = other.handlerClass;
        this.category = other.category;
    }

    public String toString() {
        return getTitle();
    }

    public HandlerCategory getCategory() {
        return category;
    }

    public final StringProperty titleProperty() {
        return this.title;
    }

    public final String getTitle() {
        return this.titleProperty().get();
    }

    public final void setTitle(final String title) {
        this.titleProperty().set(title);
    }

    public Class<?> getHandlerClass() {
        return handlerClass;
    }

    public Image getIcon() {

        switch (category) {
            default:
                String filename = "icons/" + category.name().toLowerCase() + ".png";
                return new Image(MinionHandler.class.getResourceAsStream(filename));

        }
    }

}
