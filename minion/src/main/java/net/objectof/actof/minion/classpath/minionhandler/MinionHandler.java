package net.objectof.actof.minion.classpath.minionhandler;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;


public class MinionHandler {

    public enum IconStyle {
        WHITE, BLACK
    }

    public enum IconSize {
        SIZE_16, SIZE_24, SIZE_32, SIZE_48, SIZE_64, SIZE_72, SIZE_96, SIZE_128, SIZE_256;

        public String getSize() {
            return this.name().substring(5);
        }
    }

    StringProperty title;
    Class<?> handlerClass;
    MinionHandlerCategory category;

    public MinionHandler(Class<?> cls, MinionHandlerCategory category) {
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

    public MinionHandlerCategory getCategory() {
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

    public Image getIcon(IconStyle style, IconSize size) {

        switch (category) {
            default:
                String dir = size.getSize() + "-" + style.name().toLowerCase();
                String filename = "icons/" + dir + "/" + category.name().toLowerCase() + ".png";
                return new Image(MinionHandler.class.getResourceAsStream(filename));

        }
    }
}
