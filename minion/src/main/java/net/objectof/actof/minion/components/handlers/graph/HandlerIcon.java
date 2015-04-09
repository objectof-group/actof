package net.objectof.actof.minion.components.handlers.graph;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import net.objectof.actof.minion.classpath.MinionHandler;


public class HandlerIcon extends Pane {

    Circle iconSpace;
    private ImageView icon;

    private ObjectProperty<Image> image = new SimpleObjectProperty<>(null);
    private ObjectProperty<Color> color = new SimpleObjectProperty<>(Color.BLACK);

    public HandlerIcon(MinionHandler handler, Color color) {
        setPrefSize(0, 0);

        Tooltip tip = new Tooltip(handler.getClass().getSimpleName());
        Tooltip.install(this, tip);

        icon = new ImageView();
        iconSpace = new Circle(0);

        image.addListener(change -> {
            if (image.get() == null) { return; }
            icon.setImage(image.get());

            double size = Math.max(image.get().getHeight(), image.get().getWidth());
            double offset = -(size / 2);
            icon.relocate(offset, offset);
            icon.resize(image.get().getWidth(), image.get().getHeight());
            iconSpace.setRadius(size);
        });

        this.color.addListener(change -> {
            iconSpace.setFill(this.color.get());

        });

        setImage(handler.getIcon());
        setColor(color);

        getChildren().addAll(iconSpace, icon);

    }

    public final ObjectProperty<Image> imageProperty() {
        return this.image;
    }

    public final Image getImage() {
        return this.imageProperty().get();
    }

    public final void setImage(final Image image) {
        this.imageProperty().set(image);
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
