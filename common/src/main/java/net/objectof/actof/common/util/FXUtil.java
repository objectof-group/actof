package net.objectof.actof.common.util;


import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import net.objectof.actof.common.component.feature.FXLoaded;
import net.objectof.actof.common.controller.ActofUIController;
import net.objectof.actof.common.controller.change.ChangeController;


public class FXUtil {

    public static <T extends ActofUIController> T load(Class<T> cls, String filename, ChangeController changes)
            throws IOException {

        URL url = cls.getResource(filename);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(url);
        Node node = loader.load();

        T controller = loader.getController();
        controller.setNode(node);
        controller.setChangeBus(changes);

        controller.ready();

        return controller;

    }

    public static <T extends FXLoaded> T loadFX(Class<T> cls, String filename) throws IOException {

        URL url = cls.getResource(filename);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(url);
        Region node = loader.load();

        FXLoaded controller = loader.getController();
        controller.setFXRegion(node);

        controller.onFXLoad();

        return (T) controller;

    }

}
