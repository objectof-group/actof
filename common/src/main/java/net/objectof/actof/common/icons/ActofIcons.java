package net.objectof.actof.common.icons;


import java.io.InputStream;
import java.net.URL;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class ActofIcons {

    public enum Icon {
        ADD, REMOVE, UNDO, REDO, SEARCH
    }

    public enum Size {
        BUTTON, TOOLBAR
    }

    public static ImageView getIconView(Icon icon, Size size) {
        return new ImageView(getIcon(icon, size));
    }

    public static Image getIcon(Icon icon, Size size) {
        return new Image(getIconStream(icon, size));
    }

    public static URL getIconURL(Icon icon, Size size) {
        String path = size.toString().toLowerCase() + "/" + icon.toString().toLowerCase() + ".png";
        return ActofIcons.class.getResource(path);
    }

    public static InputStream getIconStream(Icon icon, Size size) {
        String path = size.toString().toLowerCase() + "/" + icon.toString().toLowerCase() + ".png";
        return ActofIcons.class.getResourceAsStream(path);
    }
}
