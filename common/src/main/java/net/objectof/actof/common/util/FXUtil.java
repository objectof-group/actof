package net.objectof.actof.common.util;


import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import net.objectof.actof.common.controller.ActofUIController;
import net.objectof.actof.common.controller.change.ChangeController;


public class FXUtil {

    public static <T extends ActofUIController> T load(Class<T> cls, String filename, ChangeController changes)
            throws IOException {
        return load(cls, cls.getResource(filename), changes);
    }

    public static <T extends ActofUIController> T load(Class<T> cls, URL url, ChangeController changes)
            throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(url);
        Node node = loader.load();

        T controller = loader.getController();
        controller.setNode(node);
        controller.setChangeBus(changes);

        controller.ready();

        return controller;

    }
}
