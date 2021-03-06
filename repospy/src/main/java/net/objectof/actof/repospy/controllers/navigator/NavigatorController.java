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

import org.controlsfx.control.BreadCrumbBar;
import org.controlsfx.control.textfield.CustomTextField;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import net.objectof.actof.common.component.display.Panel;
import net.objectof.actof.common.component.display.impl.AbstractLoadedDisplay;
import net.objectof.actof.common.component.display.impl.IPanel;
import net.objectof.actof.common.controller.change.Change;
import net.objectof.actof.common.controller.repository.RepositoryReplacedChange;
import net.objectof.actof.common.controller.search.QueryChange;
import net.objectof.actof.common.icons.ActofIcons;
import net.objectof.actof.common.icons.Icon;
import net.objectof.actof.common.icons.Size;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.changes.EntityCreatedChange;
import net.objectof.actof.repospy.controllers.navigator.editor.layout.CompositeLayout;
import net.objectof.actof.repospy.controllers.navigator.editor.layout.IndexedLayout;
import net.objectof.actof.repospy.controllers.navigator.editor.layout.KindLayout;
import net.objectof.actof.repospy.controllers.navigator.editor.layout.MappedLayout;
import net.objectof.actof.repospy.controllers.navigator.editor.layout.PackageLayout;
import net.objectof.actof.repospy.controllers.navigator.treemodel.RepoSpyTreeItem;
import net.objectof.actof.repospy.controllers.navigator.treemodel.TreeNode;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.IAggregateNode;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.IKindNode;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.IRootNode;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;


public class NavigatorController extends AbstractLoadedDisplay {

    @FXML
    private BorderPane toppane, fieldEditor;
    @FXML
    private HBox breadcrumbToolbar, repoToolbar, optionsToolbar, searchBox, toolbar;
    @FXML
    private VBox sidebar, headerBox;
    @FXML
    private Node editorBox;

    private CustomTextField querytext;
    private ComboBox<String> kindCombo;

    @FXML
    private Button commit, review, revert;
    @FXML
    private Tooltip revert_tooltip;
    @FXML
    private TreeView<TreeNode> records;
    @FXML
    private TitledPane searchPane, entitiesPane;

    public RepoSpyController repospy;
    private boolean isQuerying = false;

    private BreadCrumbBar<TreeNode> breadcrumb;

    private RepoSpyTreeItem root;
    private IRootNode rootNode;

    @Override
    public void onFXLoad() {

    }

    private void onResourceSelect(ResourceSelectedChange change) {

        breadcrumb.setSelectedCrumb(change.getEntry());
        records.getSelectionModel().select(change.getEntry());

        RepoSpyTreeItem treeitem = change.getEntry();
        TreeNode node = treeitem.getValue();
        if (node instanceof IAggregateNode) {
            IAggregateNode resnode = (IAggregateNode) node;

            if (resnode.getStereotype() == Stereotype.INDEXED) {
                editorBox = new IndexedLayout(treeitem, repospy);
            } else if (resnode.getStereotype() == Stereotype.MAPPED) {
                editorBox = new MappedLayout(treeitem, repospy);
            } else {
                editorBox = new CompositeLayout(treeitem, repospy);
            }
        } else if (node instanceof IKindNode) {
            editorBox = new KindLayout(treeitem, repospy);
        } else if (node instanceof IRootNode) {
            editorBox = new PackageLayout(treeitem, repospy);
        } else {
            editorBox = null;
        }
        fieldEditor.setCenter(editorBox);
    }

    /* FXML Hook */
    public void doReview() throws IOException {
        repospy.showReview();
    }

    /* FXML Hook */
    public void doCommit() {
        repospy.repository.post();
    }

    /* FXML Hook */
    public void doRevert() {
        if (repospy.history.hasHistory()) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Discard Changes?");
            alert.setHeaderText("Discard uncommitted changes?");
            alert.setContentText("You cannot undo this operation.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() != ButtonType.OK) { return; }
        }
        repospy.repository.makeFresh();
    }

    public void onLoad() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Import Data");
        ExtensionFilter filter = new ExtensionFilter("JSON Data", "*.json");
        chooser.setSelectedExtensionFilter(filter);
        File file = chooser.showOpenDialog(repospy.getStage());
        if (file == null) { return; }

        List<Resource<?>> loaded;
        try {
            loaded = repospy.repository.load(new Scanner(file));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        for (Resource<?> res : loaded) {
            getChangeBus().broadcast(new EntityCreatedChange(res));
        }

    }

    /* FXML Hook */
    public void onDump() {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Dump Repository");
        ExtensionFilter filter = new ExtensionFilter("JSON Data", "*.json");
        chooser.setSelectedExtensionFilter(filter);
        File file = chooser.showOpenDialog(repospy.getStage());
        if (file == null) { return; }

        try {
            Writer writer = new FileWriter(file);
            writer.write(repospy.repository.dump());
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* FXML Hook */
    public void doConnect() throws Exception {
        repospy.onConnect();
    }

    public void recordCopy() {
        TreeItem<TreeNode> item = records.getSelectionModel().getSelectedItem();
        if (item == null) { return; }
        TreeNode entry = item.getValue();
        if (entry == null) { return; }
        Resource<?> res = entry.getRes();
        if (res == null) { return; }

        toClipboard(entry.getRes());

    }

    public void setTopController(RepoSpyController controller) {
        this.repospy = controller;

        getChangeBus().listen(this::onChange);
        getChangeBus().listen(RepositoryReplacedChange.class, this::onRepositoryReplacedChange);
        getChangeBus().listen(QueryChange.class, this::onQueryChange);
        getChangeBus().listen(ResourceSelectedChange.class, this::onResourceSelect);

        revert.setGraphic(ActofIcons.getIconView(Icon.VIEW_REFRESH, Size.TOOLBAR));
        commit.setGraphic(ActofIcons.getIconView(Icon.DOCUMENT_SAVE, Size.TOOLBAR));

        toolbar.getChildren().removeAll(repoToolbar, breadcrumbToolbar, optionsToolbar);
        headerBox.getChildren().remove(toolbar);
        repospy.getToolbars().add(repoToolbar);
        repospy.getToolbars().add(breadcrumbToolbar);
        repospy.getToolbars().add(optionsToolbar);
        Panel entitiesPanel = new IPanel("Entities", sidebar);
        entitiesPanel.setDismissible(false);
        repospy.getPanels().add(entitiesPanel);
        toppane.getChildren().remove(sidebar);

        rootNode = new IRootNode(repospy, null);
        root = new RepoSpyTreeItem(rootNode, repospy);

        breadcrumb = new BreadCrumbBar<>();
        breadcrumb.setFocusTraversable(false);
        breadcrumb.setDisable(true);
        Callback<TreeItem<TreeNode>, Button> breadCrumbFactory = breadcrumb.getCrumbFactory();
        breadcrumb.setCrumbFactory(item -> {
            Button b = breadCrumbFactory.call(item);
            b.getStyleClass().add("bread-crumb-button");
            b.setPadding(new Insets(0, 2, 0, 2));
            b.setText("");
            Label label = new Label();

            if (item.getValue() != null) {
                label.setText(item.getValue().toString());
            }

            b.setGraphic(label);
            label.setPadding(new Insets(3, 10, 3, 10));
            return b;
        });

        // breadcrumb
        breadcrumb.setAutoNavigationEnabled(false);
        breadcrumb.setOnCrumbAction(event -> {
            TreeItem<TreeNode> node = event.getSelectedCrumb();
            repospy.getChangeBus().broadcast(new ResourceSelectedChange((RepoSpyTreeItem) node));
        });
        breadcrumb.setSelectedCrumb(root);
        breadcrumbToolbar.getChildren().add(breadcrumb);

        // sidebar
        records.setShowRoot(false);
        records.setRoot(root);
        records.getSelectionModel().selectedItemProperty().addListener((ov, o, n) -> onRecordSelect(n));

        // Kind selection combobox
        kindCombo = new ComboBox<>();
        kindCombo.setMinWidth(150);
        kindCombo.valueProperty().addListener(change -> {
            repospy.search.setKind(kindCombo.getValue());
        });
        searchBox.getChildren().add(kindCombo);

        // search text field
        querytext = new CustomTextField();
        HBox.setHgrow(querytext, Priority.ALWAYS);
        searchBox.getChildren().add(querytext);
        querytext.setPromptText("Search Query");
        querytext.setOnKeyReleased(event -> {
            if (event.getCode() != KeyCode.ENTER) { return; }
            repospy.doQuery(querytext.getText());
        });

        // search clear button
        Button doclear = new Button("",
                new ImageView(new Image(NavigatorController.class.getResourceAsStream("icons/clear.png"))));
        doclear.setStyle("-fx-background-color: null; -fx-padding: 3px;");
        doclear.setOnAction(event -> {
            querytext.setText("");
            repospy.doQuery(querytext.getText());
        });
        querytext.setRight(doclear);

        // search query button
        Button doquery = new Button("", ActofIcons.getIconView(Icon.EDIT_FIND, Size.BUTTON));
        doquery.getStyleClass().add("tool-bar-button");
        doquery.setOnAction(event -> {
            repospy.doQuery(querytext.getText());
        });
        searchBox.getChildren().add(doquery);

        shortcut(toppane, () -> showSearchBar(!searchPane.isExpanded()), KeyCode.F, KeyCombination.CONTROL_DOWN);
        shortcut(searchBox, () -> showSearchBar(false), KeyCode.ESCAPE);
        shortcut(records, this::recordCopy, KeyCode.C, KeyCombination.CONTROL_DOWN);

        searchPane.sceneProperty().addListener(event -> {
            // hide title component of search titledpane. We need to force it to
            // apply css before we can look up the title component
            searchPane.applyCss();
            Pane title = (Pane) searchPane.lookup(".title");
            if (title != null) {
                title.setVisible(false);
                title.setMinHeight(0);
                title.setPrefHeight(0);
                title.setMaxHeight(0);
            }
        });

    }

    public void toggleSearchBar() {
        showSearchBar(!searchPane.isExpanded());
    }

    private void showSearchBar(boolean show) {
        searchPane.setExpanded(show);
        repospy.doQuery("");
    }

    private void onChange(Change change) {

        boolean hasHistory = repospy.history.hasHistory();
        commit.setDisable(!hasHistory);
        review.setDisable(!hasHistory);

        if (!hasHistory) {
            revert.setGraphic(ActofIcons.getIconView(Icon.VIEW_REFRESH, Size.TOOLBAR));
            revert_tooltip.setText("Refresh from repository");
        } else {
            // TODO: Fix -- this causes null pointer exception?
            revert.setGraphic(ActofIcons.getIconView(Icon.GO_FIRST, Size.TOOLBAR));
            revert_tooltip.setText("Revert all changes");
        }

    }

    private void onRepositoryReplacedChange() {
        populateEntityTree();
        populateQueryEntityChoice();
        revert.setDisable(false);
        breadcrumb.setDisable(false);

        rootNode.setPackageName(repospy.repository.getRepoName());
        getChangeBus().broadcast(new ResourceSelectedChange(root));

    }

    private void onQueryChange() {

        boolean validQuery = repospy.search.isValid();

        // if this is a valid query (maybe different than the last query),
        // or we were querying in the past (but no longer), we need to
        // repopulate the entity tree, otherwise, just refresh it
        if (validQuery) {
            querytext.setStyle("");
            populateEntityTree();
            repospy.getChangeBus().broadcast(new ResourceSelectedChange(root));
        } else {
            // if we *did* have a valid query *last time*, but not anymore,
            // repopulate the tree and send out an update
            if (isQuerying) {
                populateEntityTree();
                repospy.getChangeBus().broadcast(new ResourceSelectedChange(root));
            }
            // if this is not a valid query, and also not the empty query, mark
            // it as an error
            if (querytext.getText().length() > 0) {
                querytext.setStyle("-fx-text-box-border: red; -fx-focus-color: red ;");
            } else {
                querytext.setStyle("");
            }
        }

        isQuerying = validQuery;
    }

    private void onRecordSelect(TreeItem<TreeNode> treeItem) {

        if (treeItem == null) { return; }

        TreeNode data = treeItem.getValue();
        if (data == null) { return; }
        getChangeBus().broadcast(new ResourceSelectedChange((RepoSpyTreeItem) treeItem));

    }

    private void populateQueryEntityChoice() {
        kindCombo.getItems().clear();
        for (Kind<?> kind : repospy.repository.getEntities()) {
            kindCombo.getItems().add(kind.getComponentName());
        }
    }

    private void populateEntityTree() {
        root.updateChildren();
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

    public static NavigatorController load() throws IOException {
        return FXUtil.loadFX(NavigatorController.class, "Navigator.fxml");
    }

    @Override
    public String getTitle() {
        return "RepoSpy";
    }

}
