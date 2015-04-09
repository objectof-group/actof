package net.objectof.actof.minion.components.handlers.graph;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import net.objectof.actof.minion.common.minionhandler.MinionHandler;
import net.objectof.actof.minion.common.minionhandler.MinionHandler.IconSize;
import net.objectof.actof.minion.common.minionhandler.MinionHandler.IconStyle;


public class HandlerIcon extends Pane {

    Circle iconSpace;
    private ImageView icon;

    private ObjectProperty<Image> image = new SimpleObjectProperty<>(null);

    public HandlerIcon(MinionHandler handler) {
        setPrefSize(0, 0);

        Tooltip tip = new Tooltip();
        String handlerClass = handler.getHandlerClass().getSimpleName();
        tip.textProperty().bind(handler.titleProperty().concat(" (").concat(handlerClass).concat(")"));
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

        iconSpace.fillProperty().bind(handler.colorProperty());
        setImage(handler.getIcon(IconStyle.WHITE, IconSize.SIZE_24));

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

}
