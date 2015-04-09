package net.objectof.actof.minion.common.minionhandler;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import net.objectof.actof.widgets.network.INetworkVertex;


public class MinionHandler extends INetworkVertex {

    public enum Category {
        ADAPTER, AUTHENTICATOR, AUTHORIZER, BRANCH, BROADCASTER, COLLECTOR, ENDPOINT, FORWARDER, GENERIC, HIERARCHY, INSTRUMENTATION, INTERFACE, PERSIST, QUEUE, REGISTER, ROUTER, SCHEDULE, SERVICE, STEP, STREAMING, TIMER, TRANSFORM;
    }

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
    Category category;

    private ObjectProperty<Color> color = new SimpleObjectProperty<Color>(MinionHandlerColor.BLUE.toFXColor());

    public MinionHandler(Class<?> cls, Category category) {
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

    public Category getCategory() {
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
                System.out.println(filename);
                return new Image(MinionHandler.class.getResourceAsStream(filename));

        }
    }

    public final ObjectProperty<Color> colorProperty() {
        return this.color;
    }

    public final Color getColor() {
        return this.colorProperty().get();
    }

    public final void setColor(final Color color) {
        this.colorProperty().set(color);
    }
}
