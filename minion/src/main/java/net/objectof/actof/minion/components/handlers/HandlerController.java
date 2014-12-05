package net.objectof.actof.minion.components.handlers;


import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebView;
import net.objectof.actof.common.controller.IActofUIController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.minion.components.handlers.ui.HandlerCell;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.IHandler;
import netscape.javascript.JSObject;


public class HandlerController extends IActofUIController {

    @FXML
    ListView<HandlerClass> handlers;
    HandlerLoader loader = new HandlerLoader();

    @FXML
    private WebView webview;
    @FXML
    private TextField address;

    @Override
    @FXML
    protected void initialize() throws IOException, URISyntaxException {

        address.setOnKeyPressed(event -> {
            if (event.getCode() != KeyCode.ENTER) { return; }
            webview.getEngine().load(address.getText());
        });

        JSObject jsobj = (JSObject) webview.getEngine().executeScript("window");
        jsobj.setMember("java", this);

        File icondir = new File(getClass().getResource("icons/").toURI());
        List<File> files = new ArrayList<>(Arrays.asList(icondir.listFiles()));
        files.sort((a, b) -> {
            return a.compareTo(b);
        });
        for (File icon : files) {
            HandlerClass handler = new HandlerClass((Class<IHandler<? extends HttpRequest>>) (Object) IHandler.class,
                    icon.getName(), icon);
            handlers.getItems().add(handler);
        }

        handlers.setCellFactory(view -> {
            return new HandlerCell();
        });

    }

    @Override
    public void ready() {}


    public void addJar(File jar) {
        try {
            loader.loadJar(jar);
            handlers.getItems().setAll(loader.getHandlers());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static HandlerController load(ChangeController changes) throws IOException {
        return FXUtil.load(HandlerController.class, "Handlers.fxml", changes);
    }




}
