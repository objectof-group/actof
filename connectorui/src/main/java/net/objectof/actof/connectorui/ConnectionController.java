package net.objectof.actof.connectorui;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import net.objectof.actof.common.controller.IActofUIController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.change.IChangeController;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.connectorui.parametereditor.FilenameParameterEditor;
import net.objectof.actof.connectorui.parametereditor.ParameterEditor;
import net.objectof.actof.connectorui.parametereditor.PasswordParameterEditor;
import net.objectof.actof.connectorui.parametereditor.TextParameterEditor;
import net.objectof.actof.widgets.KeyValuePane;
import net.objectof.connector.AbstractConnector;
import net.objectof.connector.Connector;
import net.objectof.connector.Connectors;
import net.objectof.connector.parameter.Parameter;

import org.controlsfx.dialog.Dialogs;


public class ConnectionController extends IActofUIController {

    private boolean connectSelected = false;
    private Stage stage;
    private boolean create;

    ComboBox<String> repoChoice;

    @FXML
    private GridPane grid;
    @FXML
    private BorderPane window;
    @FXML
    private VBox gridBox;
    @FXML
    private ChoiceBox<Connector> backend;

    @FXML
    Button connect, cancel;

    public static Connector showConnectDialog(Window owner) throws IOException {
        return showDialog(owner, false);
    }

    @Override
    @FXML
    protected void initialize() {
        try {
            populateChoiceBox();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ready() {}

    private void populateChoiceBox() {
        populateChoiceBox(null);
    }

    private void populateChoiceBox(String name) {

        backend.getItems().clear();

        // load any saved connections
        backend.getItems().addAll(SavedConnections.getSavedConnectors());

        // Because we've used generics for this ChoiceBox everywhere else, we
        // need to cast it to a non-generic choicebox in order to get it to
        // accept a separator
        ((ChoiceBox) backend).getItems().add(new Separator());

        // load new/template connections
        for (Connector newconn : Connectors.getConnectors()) {
            ConnectorUI newconnui = new ConnectorUI(newconn);
            newconnui.setDisplayName("New " + newconn.getType() + " Connection");
            backend.getItems().add(newconnui);
        }

        backend.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            layout();
        });

        ConnectorUI last = SavedConnections.getLastConnector();
        ConnectorUI requested = SavedConnections.getSavedByName(name);

        // if there is a last connection, make sure it's in the list if it isn't
        // already
        if (last != null) {
            if (!backend.getItems().contains(last)) {
                // last.setDisplayName("< Last Connection >");
                backend.getItems().add(0, last);
            }
        }

        if (requested != null) {
            backend.getSelectionModel().select(requested);
        } else if (last != null) {
            for (Object o : backend.getItems()) {
                if (!(o instanceof Connector)) {
                    continue;
                }
                Connector conn = (Connector) o;
                if (conn.equals(last)) {
                    backend.getSelectionModel().select(conn);
                }
            }
        } else {
            backend.getSelectionModel().select(0);
        }

    }

    public void add() {
        Optional<String> name = Dialogs.create().title("Connection Name").message("Enter a name for this connection")
                .showTextInput();
        if (!name.isPresent()) { return; }

        Connector connector = getConnector();
        connector.setName(name.get());
        SavedConnections.addSavedConnector(connector);

        populateChoiceBox(name.get());

    }

    public void remove() {
        SavedConnections.removeSavedConnector(getConnector());
        populateChoiceBox();
    }

    private Connector getSelectedConnector() {
        return (Connector) backend.getSelectionModel().getSelectedItem();
    }

    private void layout() {
        grid.getChildren().clear();

        int row = 0;
        Connector conn = getSelectedConnector();
        ParameterEditor editor = null;
        if (conn == null) { return; }
        
        for (Parameter parameter : conn.getParameters()) {
            if (parameter.getType() == null) {
                continue;
            }
            // We do something special for this parameter below
            if (parameter.getTitle().equals(AbstractConnector.KEY_REPOSITORY)) {
                continue;
            }

            switch (parameter.getType()) {
                case FILE:
                    editor = new FilenameParameterEditor(parameter, stage);
                    break;
                case INT:
                    editor = new TextParameterEditor(parameter);
                    break;
                case PASSWORD:
                    editor = new PasswordParameterEditor(parameter);
                    break;
                case REAL:
                    editor = new TextParameterEditor(parameter);
                    break;
                case STRING:
                    editor = new TextParameterEditor(parameter);
                    break;
                default:
                    break;
            }

            editor.setCreate(create);

            Label label = new Label(parameter.getTitle());
            label.setStyle("-fx-text-fill: #666;");
            grid.add(label, 0, row);
            grid.add(editor.asNode(), 1, row++);
        }

        // Repository parameter
        Parameter repoParameter = conn.getParameter(AbstractConnector.KEY_REPOSITORY);
        repoChoice = new ComboBox<>();
        updateRepoChoice(repoParameter.getValue());
        repoChoice.getSelectionModel().select(repoParameter.getValue());
        repoChoice.focusedProperty().addListener(change -> updateRepoChoice(repoParameter.getValue()));
        repoChoice.getSelectionModel().selectedItemProperty().addListener(change -> {
            String selection = repoChoice.getSelectionModel().getSelectedItem();
            if (selection == null) { return; }
            repoParameter.setValue(selection);
        });

        AnchorPane repoAnchor = new AnchorPane();
        AnchorPane.setBottomAnchor(repoChoice, 0d);
        AnchorPane.setTopAnchor(repoChoice, 0d);
        AnchorPane.setLeftAnchor(repoChoice, 0d);
        AnchorPane.setRightAnchor(repoChoice, 0d);
        repoAnchor.getChildren().add(repoChoice);

        Label label = new Label(repoParameter.getTitle());
        label.setStyle("-fx-text-fill: #666;");
        grid.add(label, 0, row);
        grid.add(repoAnchor, 1, row++);
        

        if (stage != null) {
            stage.sizeToScene();
        }

    }

    private void updateRepoChoice(String def) {
        try {
            String selection = repoChoice.getSelectionModel().getSelectedItem();
            repoChoice.getItems().setAll(getSelectedConnector().getSchemaNames());
            if (selection != null) {
                repoChoice.getSelectionModel().select(selection);
            } else {
                repoChoice.getSelectionModel().select(def);
            }
        }
        catch (Exception e) {
            repoChoice.getItems().clear();
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.stage = dialogStage;
        stage.setResizable(false);
    }

    public void cancel() {
        if (stage != null) stage.close();
    }

    public void connect() {

        connectSelected = true;

        Connector conn = getSelectedConnector();
        SavedConnections.setLastConnector(conn);

        getChangeBus().broadcast(new ConnectionSelectedChange(conn));
        if (stage != null) {
            stage.close();
        }

    }

    public Connector getConnector() {
        return getSelectedConnector();
    }

    private boolean isConnectSelected() {
        return connectSelected;
    }

    public static ConnectionController load(boolean create) throws IOException {
        return load(create, new IChangeController());
    }

    public static ConnectionController load(boolean create, ChangeController changes) throws IOException {

        ConnectionController controller = FXUtil.load(ConnectionController.class, "ConnectionDialog.fxml", changes);

        controller.create = create;
        if (create) {
            controller.connect.setText("Create");
        }
        controller.cancel.setVisible(false);

        return controller;

    }

    public static Connector showDialog(Window owner, boolean create) throws IOException {

        ConnectionController controller = load(create, new IChangeController());

        Stage connectStage = new Stage(StageStyle.DECORATED);
        connectStage.setTitle(create ? "Create Repository" : "Connect to Repository");
        connectStage.initModality(Modality.APPLICATION_MODAL);
        connectStage.initOwner(owner);
        connectStage.setScene(new Scene((Parent) controller.getNode()));
        controller.setDialogStage(connectStage);

        controller.cancel.setVisible(true);

        connectStage.showAndWait();
        if (controller.isConnectSelected()) {
            return controller.getConnector();
        } else {
            return null;
        }
    }

}
