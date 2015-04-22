package net.objectof.actof.connectorui;


import java.io.IOException;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import net.objectof.actof.common.controller.IActofUIController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.change.IChangeController;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.connectorui.parametereditor.DirectoryParameterEditor;
import net.objectof.actof.connectorui.parametereditor.FilenameParameterEditor;
import net.objectof.actof.connectorui.parametereditor.ParameterEditor;
import net.objectof.actof.connectorui.parametereditor.PasswordParameterEditor;
import net.objectof.actof.connectorui.parametereditor.TextParameterEditor;
import net.objectof.actof.widgets.KeyValuePane;
import net.objectof.connector.AbstractConnector;
import net.objectof.connector.Connector;
import net.objectof.connector.Connectors;
import net.objectof.connector.Parameter;

import org.controlsfx.dialog.Dialogs;


public class ConnectionController extends IActofUIController {

    private boolean connectSelected = false;
    private Stage stage;
    private boolean create;

    private ComboBox<String> repoChoice;
    private KeyValuePane grid = new KeyValuePane();

    @FXML
    private BorderPane window;
    @FXML
    private AnchorPane gridBox;
    @FXML
    private ComboBox<ConnectorUI> backend;

    @FXML
    Button connect, cancel;

    public static Connector showConnectDialog(Window owner) throws IOException {
        return showDialog(owner, false);
    }

    @Override
    @FXML
    protected void initialize() {}

    @Override
    public void ready() {

        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(10));
        grid.setKeyVAlignment(VPos.CENTER);
        AnchorPane.setBottomAnchor(grid, 0d);
        AnchorPane.setTopAnchor(grid, 0d);
        AnchorPane.setLeftAnchor(grid, 0d);
        AnchorPane.setRightAnchor(grid, 0d);
        gridBox.getChildren().add(grid);

        try {
            populateChoiceBox();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        backend.setButtonCell(new ConnectorCell());
        backend.setCellFactory(listview -> new ConnectorCell());
    }

    private void populateChoiceBox() {
        populateChoiceBox(null);
    }

    private void populateChoiceBox(String name) {

        backend.getItems().clear();

        // load any saved connections
        backend.getItems().addAll(SavedConnections.getSavedConnectors());

        // load new/template connections
        for (Connector newconn : Connectors.getConnectors()) {
            ConnectorUI newconnui = new ConnectorUI(newconn);
            newconnui.setDisplayName("New " + newconn.getType() + " Connection");
            newconnui.setTemplate(true);
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
                ConnectorUI conn = (ConnectorUI) o;
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
        grid.clear();

        Connector conn = getSelectedConnector();
        ParameterEditor editor = null;
        if (conn == null) { return; }

        for (Parameter parameter : conn.getParameters()) {
            // We do something special for this parameter below
            if (parameter.getTitle().equals(AbstractConnector.KEY_REPOSITORY)) {
                continue;
            }

            editor = getEditorForHint(parameter);
            editor.setCreate(create);
            grid.put(parameter.getTitle(), editor.asNode());
        }

        // Repository parameter
        Parameter repoParameter = conn.getParameter(AbstractConnector.KEY_REPOSITORY);
        repoChoice = new ComboBox<>();
        if (create) {
            repoChoice.setEditable(true);
        }
        updateRepoChoice(repoParameter.getValue());
        repoChoice.getSelectionModel().select(repoParameter.getValue());
        repoChoice.focusedProperty().addListener(change -> {
            if (!repoChoice.isFocused()) { return; }
            updateRepoChoice(repoParameter.getValue());
        });
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

        grid.put(repoParameter.getTitle(), repoAnchor);

        if (stage != null) {
            stage.sizeToScene();
        }

    }

    public ParameterEditor getEditorForHint(Parameter parameter) {

        if (parameter.getHint() == null) { return new TextParameterEditor(parameter); }

        switch (parameter.getHint()) {
            case FILE:
                return new FilenameParameterEditor(parameter, stage);
            case DIRECTORY:
                return new DirectoryParameterEditor(parameter, stage);
            case PASSWORD:
                return new PasswordParameterEditor(parameter);
            default:
                return new TextParameterEditor(parameter);
        }

    }

    private void updateRepoChoice(String def) {
        try {
            String selection = repoChoice.getSelectionModel().getSelectedItem();
            repoChoice.getItems().setAll(getSelectedConnector().getRepositoryNames());
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

    public static ConnectionController load() throws IOException {
        return load(new IChangeController());
    }

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        connect.setText("Create");
        this.create = create;
        layout();
    }

    public static ConnectionController load(ChangeController changes) throws IOException {

        ConnectionController controller = FXUtil.load(ConnectionController.class, "ConnectionDialog.fxml", changes);
        controller.cancel.setVisible(false);
        return controller;

    }

    public static Connector showDialog(Window owner, boolean create) throws IOException {

        ConnectionController controller = load(new IChangeController());
        controller.setCreate(create);

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

class ConnectorCell extends TextFieldListCell<ConnectorUI> {

    @Override
    public void updateItem(ConnectorUI item, boolean empty) {
        super.updateItem(item, empty);

        ImageView node;
        if (!empty && item.isTemplate()) {
            node = new ImageView(new Image(ConnectionController.class.getResourceAsStream("icons/blank.png")));
        } else {
            node = new ImageView(new Image(ConnectionController.class.getResourceAsStream("icons/repo.png")));
        }
        this.setGraphic(node);
    }
}
