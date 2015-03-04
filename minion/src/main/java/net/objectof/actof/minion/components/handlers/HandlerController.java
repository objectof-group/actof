package net.objectof.actof.minion.components.handlers;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebView;
import net.objectof.actof.common.controller.IActofUIController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.minion.classpath.MinionHandler;
import net.objectof.actof.minion.classpath.sources.MinionSource;
import net.objectof.actof.minion.components.classpath.change.ClasspathChange;
import net.objectof.actof.minion.components.handlers.ui.HandlerCell;
import netscape.javascript.JSObject;


public class HandlerController extends IActofUIController {

    @FXML
    TableView<MinionHandler> handlers;

    @FXML
    TableColumn<MinionHandler, MinionHandler> handlerColumn;

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

        handlerColumn.setCellFactory(column -> new HandlerCell());
        handlerColumn.setCellValueFactory(value -> new SimpleObjectProperty<MinionHandler>(value.getValue()));

    }

    @Override
    public void ready() {
        getChangeBus().listen(ClasspathChange.class, this::setHandlers);
    }

    private void setHandlers(ClasspathChange change) {
        try {
            _setHandlers(change);
        }
        catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void _setHandlers(ClasspathChange change) throws URISyntaxException, MalformedURLException {

        handlers.getItems().clear();

        for (MinionSource source : change.getClasspath()) {
            for (MinionHandler handler : source.getHandlers()) {
                File icon = new File(getClass().getResource("icons/generic-24.png").toURI());
                handler.setIcon(icon);
                handlers.getItems().add(handler);
            }
        }

    }

    public static HandlerController load(ChangeController changes) throws IOException {
        return FXUtil.load(HandlerController.class, "Handlers.fxml", changes);
    }

}
