package net.objectof.actof.web.client;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.adapter.JavaBeanObjectProperty;
import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.util.StringConverter;
import net.objectof.actof.common.component.display.Display;
import net.objectof.actof.common.component.editor.impl.AbstractLoadedEditor;
import net.objectof.actof.common.component.resource.Resource;
import net.objectof.actof.common.controller.change.Change;
import net.objectof.actof.common.icons.ActofIcons;
import net.objectof.actof.common.icons.Icon;
import net.objectof.actof.common.icons.Size;
import net.objectof.actof.common.util.ActofSerialize;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.web.server.change.ServerStartChange;
import net.objectof.actof.web.server.change.ServerStopChange;
import net.objectof.actof.widgets.StatusLight;
import net.objectof.actof.widgets.StatusLight.Status;


public class WebClient extends AbstractLoadedEditor implements Display {

    @FXML
    private BorderPane topPane;

    @FXML
    private TabPane tabpane;

    @FXML
    private Tab tabcookie, tabrequest, tabresponse, tabheaders;

    @FXML
    private TextArea output, body;

    @FXML
    private TextField address, newcookiekey, newcookievalue;

    @FXML
    private Label prefixlabel;

    @FXML
    private BorderPane responsebodypane;

    @FXML
    private ChoiceBox<String> method;

    @FXML
    private Button go;

    @FXML
    private HBox toolbar;

    @FXML
    private TreeTableView<KV> cookies, headers;
    @FXML
    private TreeTableColumn<KV, String> cookieskey, cookiesvalue, headerskey, headersvalue;

    private StatusLight responsemessage = new StatusLight("No Response");

    private List<KV> cookielist = new ArrayList<>();
    private String prefix;

    @Override
    public void construct() {

        setTitle("REST Client");

        responsebodypane.setTop(responsemessage);

        method.getItems().addAll("GET");
        method.getItems().addAll("POST");
        method.getSelectionModel().select(0);

        address.setOnKeyReleased(event -> {
            if (event.getCode() != KeyCode.ENTER) { return; }
            onGo();
        });

        output.setFont(Font.font("Monospaced"));
        body.setFont(Font.font("Monospaced"));

        cookies.setRoot(new TreeItem<>());
        cookieskey.setCellValueFactory(cookie -> {
            return wrap(cookie.getValue().getValue(), "key");
        });
        cookiesvalue.setCellValueFactory(cookie -> {
            return wrap(cookie.getValue().getValue(), "value");
        });
        cookiesvalue.setCellFactory(column -> {
            return new TextFieldTreeTableCell<KV, String>(new StringConverter<String>() {

                @Override
                public String toString(String object) {
                    return object;
                }

                @Override
                public String fromString(String string) {
                    return string;
                }
            });
        });

        headers.setRoot(new TreeItem<>());
        headers.setShowRoot(false);
        headerskey.setCellValueFactory(cookie -> {
            return new SimpleStringProperty(cookie.getValue().getValue().key);
        });
        headersvalue.setCellValueFactory(cookie -> {
            if (cookie == null) { return null; }
            return new SimpleStringProperty(cookie.getValue().getValue().value);
        });

        headersvalue.setCellFactory(column -> {
            return new TextFieldTreeTableCell<KV, String>(new StringConverter<String>() {

                @Override
                public String toString(String object) {
                    return object;
                }

                @Override
                public String fromString(String string) {
                    return string;
                }
            });
        });

        serverStopped(new ServerStopChange());
        tabpane.getSelectionModel().select(tabresponse);

        go.setGraphic(ActofIcons.getIconView(Icon.MEDIA_PLAYBACK_START, Size.TOOLBAR));

        getToolbars().addAll(toolbar.getChildren());
        toolbar.getChildren().clear();
        topPane.setTop(null);

        getChangeBus().listen(this::onChange);

    }

    private void onChange(Change change) {
        change.when(ServerStartChange.class, this::serverStarted);
        change.when(ServerStopChange.class, this::serverStopped);
    }

    public void onGo() {
        switch (method.getSelectionModel().getSelectedItem()) {
            case "GET":
                doGet();
                break;
            case "POST":
                doPost();
                break;
        }

        tabpane.getSelectionModel().select(tabresponse);

    }

    private void serverStarted(ServerStartChange change) {
        setPrefix(change.getUrl());
        address.setDisable(false);
        go.setDisable(false);
        method.setDisable(false);
        prefixlabel.setDisable(false);
        tabpane.setDisable(false);
    }

    private void serverStopped(ServerStopChange chagne) {
        setPrefix("Server Not Running");
        address.setDisable(true);
        go.setDisable(true);
        method.setDisable(true);
        prefixlabel.setDisable(true);
        tabpane.setDisable(true);
    }

    private Builder getBuilder() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(prefix + address.getText());
        Builder builder = target.request();
        for (KV c : cookielist) {
            builder.cookie(new Cookie(c.key, c.value));
        }
        return builder;
    }

    private void doGet() {
        Response response = getBuilder().get();
        setClientResponse(response);
    }

    private void doPost() {
        Response response = getBuilder().post(Entity.text(body.getText()));
        setClientResponse(response);
    }

    private void setClientResponse(Response response) {

        setOutput(response.readEntity(String.class));

        int statuscode = response.getStatus();
        String status = response.getStatusInfo().getReasonPhrase();
        status += " (" + response.getStatus() + ")";

        if (statuscode <= 299) {
            responsemessage.setStatus(Status.GOOD, status);
        } else if (statuscode <= 399) {
            responsemessage.setStatus(Status.MAYBE, status);
        } else {
            responsemessage.setStatus(Status.BAD, status);
        }

        List<TreeItem<KV>> headerlist = headers.getRoot().getChildren();
        headerlist.clear();
        for (String key : response.getHeaders().keySet()) {
            List<Object> sublist = response.getHeaders().get(key);
            for (Object val : sublist) {
                KV kv = new KV(key, val.toString());
                headerlist.add(new TreeItem<>(kv));
            }
        }
    }

    private void setOutput(String msg) {
        msg = prettyprint(msg);
        output.setText(msg);
    }

    private String prettyprint(String json) {
        Object o = ActofSerialize.deserialize(json);
        return ActofSerialize.serialize(o);
    }

    private void setPrefix(String prefix) {
        this.prefix = prefix;
        prefixlabel.setText(prefix);
    }

    public void onRemoveCookie() {
        // TODO:
    }

    public void onAddCookie() {
        KV cookie = new KV(newcookiekey.getText(), newcookievalue.getText());
        cookielist.add(cookie);
        cookies.getRoot().getChildren().add(new TreeItem<KV>(cookie));
    }

    public static WebClient load() throws IOException {
        return FXUtil.loadFX(WebClient.class, "WebClient.fxml");
    }

    private <T> JavaBeanObjectProperty<T> wrap(Object bean, String field) {
        JavaBeanObjectPropertyBuilder<T> builder = JavaBeanObjectPropertyBuilder.create();
        builder.bean(bean);
        builder.name(field);
        try {
            return builder.build();
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class KV {

        public String key, value;

        public KV(String k, String v) {
            key = k;
            value = v;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    @Override
    public Display getDisplay() {
        return this;
    }

    @Override
    protected void onResourceAdded(Resource res) throws Exception {}

}
