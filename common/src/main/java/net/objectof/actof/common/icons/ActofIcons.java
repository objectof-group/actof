package net.objectof.actof.common.icons;


import java.io.InputStream;
import java.net.URL;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class ActofIcons {

    public static ImageView getCustomIcon(Class<?> relativeTo, String name) {
        return new ImageView(new Image(relativeTo.getResourceAsStream(name)));
    }

    public static ImageView getIconView(Icon icon, Size size) {
        return new ImageView(getIcon(icon, size));
    }

    public static Image getIcon(Icon icon, Size size) {
        return new Image(getIconStream(icon, size));
    }

    public static URL getIconURL(Icon icon, Size size) {
        String path = size.toString().toLowerCase() + "/" + icon.toString().toLowerCase().replace("_", "-") + ".png";
        return ActofIcons.class.getResource(path);
    }

    public static InputStream getIconStream(Icon icon, Size size) {
        String path = size.toString().toLowerCase() + "/" + icon.toString().toLowerCase().replace("_", "-") + ".png";
        return ActofIcons.class.getResourceAsStream(path);
    }
}
