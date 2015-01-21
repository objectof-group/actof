package net.objectof.actof.repospy.controllers.navigator;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCombination.Modifier;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import net.objectof.actof.common.controller.IActofUIController;
import net.objectof.actof.common.controller.change.Change;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.repository.RepositoryReplacedChange;
import net.objectof.actof.common.controller.search.QueryChange;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.changes.EntityCreatedChange;
import net.objectof.actof.repospy.controllers.navigator.composite.CompositeEntry;
import net.objectof.actof.repospy.controllers.navigator.composite.CompositeTreeItem;
import net.objectof.actof.repospy.controllers.navigator.composite.CompositeTreeTableCell;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.TextEditor;
import net.objectof.actof.repospy.controllers.navigator.kind.KindTreeEntry;
import net.objectof.actof.repospy.controllers.navigator.kind.KindTreeItem;
import net.objectof.connector.Connector;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;

import org.controlsfx.dialog.Dialogs;


public class NavigatorController extends IActofUIController {

    public RepoSpyController repospy;
    private boolean isQuerying = false;

    @FXML
    private TreeTableView<CompositeEntry> table;

    @FXML
    private TreeTableColumn<CompositeEntry, String> field;
    @FXML
    private TreeTableColumn<CompositeEntry, CompositeEntry> value;
    @FXML
    private TreeTableColumn<CompositeEntry, String> stereotype;

    @FXML
    private TextField querytext;
    @FXML
    private Button connect;
    @FXML
    private Button commit;
    @FXML
    private Button review;
    @FXML
    private Button revert;
    @FXML
    private Button addEntity;
    @FXML
    private Button dump, load;
    @FXML
    private ChoiceBox<String> queryEntity;

    @FXML
    private ImageView revert_image;
    @FXML
    private Tooltip revert_tooltip;

    @FXML
    private TreeView<KindTreeEntry> records;

    @Override
    @FXML
    protected void initialize() {

        TreeItem<KindTreeEntry> root = new TreeItem<>();
        records.setShowRoot(false);
        records.setRoot(root);
        records.getSelectionModel().selectedItemProperty().addListener((ov, o, n) -> onRecordSelect(n));

        table.setShowRoot(false);

        value.setCellValueFactory(value -> value.getValue().getValue());

        value.setCellFactory(column -> {
            return new CompositeTreeTableCell();
        });
        value.setEditable(true);

        field.setCellValueFactory(data -> {
            CompositeEntry entry = data.getValue().getValue();
            if (entry == null || entry.key == null) { return new SimpleStringProperty("null"); }
            return new SimpleStringProperty(entry.key.toString());
        });

        stereotype.setCellValueFactory(data -> {
            CompositeEntry entry = data.getValue().getValue();
            if (entry == null) { return new SimpleStringProperty(""); }
            Stereotype st = entry.getStereotype();
            return new SimpleStringProperty(st.toString());
        });

        querytext.setOnKeyReleased(event -> {
            if (event.getCode() != KeyCode.ENTER) { return; }
            repospy.doQuery(querytext.getText());
        });

        queryEntity.valueProperty().addListener(change -> {
            repospy.search.setKind(queryEntity.getValue());
        });

        shortcut(table, this::tableCopy, KeyCode.C, KeyCombination.CONTROL_DOWN);
        shortcut(records, this::recordCopy, KeyCode.C, KeyCombination.CONTROL_DOWN);

    }

    @Override
    public void ready() {
        getChangeBus().listen(this::onChange);
    }

    /* FXML Hook */
    public void doReview() throws IOException {
        repospy.showReview(repospy.history.get());
    }

    /* FXML Hook */
    public void doCommit() {
        repospy.repository.post();
    }

    /* FXML Hook */
    public void doRevert() {
        repospy.repository.makeFresh();
        refreshEntityTree();
    }

    public void onLoad() throws FileNotFoundException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Import Data");
        ExtensionFilter filter = new ExtensionFilter("JSON Data", "*.json");
        chooser.setSelectedExtensionFilter(filter);
        File file = chooser.showOpenDialog(repospy.primaryStage);
        if (file == null) { return; }

        repospy.repository.load(new Scanner(file));

    }

    /* FXML Hook */
    public void onDump() throws IOException {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Dump Repository");
        ExtensionFilter filter = new ExtensionFilter("JSON Data", "*.json");
        chooser.setSelectedExtensionFilter(filter);
        File file = chooser.showOpenDialog(repospy.primaryStage);
        if (file == null) { return; }

        Writer writer = new FileWriter(file);
        writer.write(repospy.repository.dump());
        writer.close();
    }

    /* FXML Hook */
    public void doAddEntity() {

        Optional<String> kind = Dialogs
                .create()
                .title("Create New Entry")
                // .masthead("Create New Entry")
                .message("Entity Kind")
                .showChoices(
                        repospy.repository.getEntities().stream().map(k -> k.getComponentName())
                                .collect(Collectors.toList()));

        if (!kind.isPresent()) { return; }

        Resource<?> newEntity = repospy.repository.getStagingTx().create(kind.get());
        EntityCreatedChange change = new EntityCreatedChange(newEntity);
        repospy.getChangeBus().broadcast(change);
        repospy.repository.addTransientEntity(newEntity);
        refreshEntityTree();

    }

    /* FXML Hook */
    public void doConnect() throws Exception {
        Connector conn = repospy.showConnect();
        if (conn == null) { return; }

        try {
            repospy.connect(conn);
        }
        catch (Exception e) {
            Dialogs.create().title("Connection Failed").message("Failed to connect to the specified repository")
                    .showException(e);
        }
    }

    public void recordCopy() {
        TreeItem<KindTreeEntry> item = records.getSelectionModel().getSelectedItem();
        if (item == null) { return; }
        KindTreeEntry entry = item.getValue();
        if (entry == null) { return; }
        Resource<?> res = entry.res;
        if (res == null) { return; }

        toClipboard(entry.res);

    }

    public void tableCopy() {
        TreeItem<CompositeEntry> item = table.getSelectionModel().getSelectedItem();
        if (item == null) { return; }
        CompositeEntry entry = item.getValue();
        if (entry == null) { return; }
        Object o = entry.getFieldValue();
        if (o == null) { return; }

        toClipboard(entry.getFieldValue());

    }

    public void setTopController(RepoSpyController controller) {
        this.repospy = controller;
    }

    private void onChange(Change change) {

        boolean hasHistory = repospy.history.get().size() > 0;
        commit.setDisable(!hasHistory);
        review.setDisable(!hasHistory);

        Image image;
        if (!hasHistory) {
            image = new Image(getClass().getResourceAsStream("icons/refresh.png"));
            revert_image.setImage(image);
            revert_tooltip.setText("Refresh from repository");
        } else {
            image = new Image(getClass().getResourceAsStream("icons/revert.png"));
            revert_image.setImage(image);
            revert_tooltip.setText("Revert all changes");
        }

        change.when(RepositoryReplacedChange.class, this::onRepositoryReplacedChange);
        change.when(QueryChange.class, this::onQueryChange);

    }

    private void onRepositoryReplacedChange() {
        populateEntityTree();
        populateQueryEntityChoice();
        addEntity.setDisable(false);
        revert.setDisable(false);
        dump.setDisable(false);
        load.setDisable(false);
    }

    private void onQueryChange() {

        boolean validQuery = repospy.search.isValid();

        // if this is a valid query (maybe different than the last query),
        // or we were querying in the past (but no longer), we need to
        // repopulate the entity tree, otherwise, just refresh it
        if (validQuery || isQuerying) {
            querytext.setStyle("");
            populateEntityTree();
        } else {
            if (querytext.getText().length() > 0) {
                querytext.setStyle(TextEditor.redborder);
            } else {
                querytext.setStyle("");
            }
            refreshEntityTree();
        }

        isQuerying = validQuery;
    }

    private void onRecordSelect(TreeItem<KindTreeEntry> treeItem) {

        if (treeItem == null) {
            table.setRoot(new TreeItem<CompositeEntry>());
            return;
        }

        KindTreeEntry data = treeItem.getValue();
        if (data == null || data.res == null) {
            table.setRoot(new TreeItem<CompositeEntry>());
            return;
        }

        Resource<?> record = data.res;

        CompositeEntry rootEntry = new CompositeEntry(repospy, record.id(), record.id().kind(), "Resource", true);
        CompositeTreeItem root = new CompositeTreeItem(rootEntry, repospy);
        root.setExpanded(true);

        table.setRoot(root);

    }

    private void populateQueryEntityChoice() {
        queryEntity.getItems().clear();
        for (Kind<?> kind : repospy.repository.getEntities()) {
            queryEntity.getItems().add(kind.getComponentName());
        }
    }

    private void populateEntityTree() {

        List<Kind<?>> entities = repospy.repository.getEntities();

        // repopulate tree
        TreeItem<KindTreeEntry> root = records.getRoot();
        root.getChildren().clear();
        for (Kind<?> kind : entities) {

            String kindname = kind.getComponentName();
            if (repospy.search.isValid() && !repospy.search.getKind().equals(kindname)) {
                continue;
            }

            KindTreeItem item = new KindTreeItem(kind.getComponentName(), repospy);
            item.setExpanded(repospy.search.isValid());
            root.getChildren().add(item);

        }

    }

    private void refreshEntityTree() {
        TreeItem<KindTreeEntry> root = records.getRoot();
        for (TreeItem<KindTreeEntry> item : root.getChildren()) {
            KindTreeItem child = (KindTreeItem) item;
            child.updateChildren();
        }
    }

    private void toClipboard(Object o) {

        String data = o.toString();
        if (o instanceof Resource) {
            StringWriter writer = new StringWriter();
            ((Resource<?>) o).send("application/json", writer);
            data = writer.toString();
        }

        Clipboard cb = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(data);
        cb.setContent(content);
    }

    private void shortcut(Node node, Runnable runnable, KeyCode key, Modifier... modifiers) {
        KeyCodeCombination keycode = new KeyCodeCombination(key, modifiers);
        node.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (keycode.match(event)) {
                runnable.run();
            }
        });
    }

    public static NavigatorController load(ChangeController changes) throws IOException {
        return FXUtil.load(NavigatorController.class, "Navigator.fxml", changes);
    }

}
