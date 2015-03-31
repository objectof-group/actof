package net.objectof.actof.schemaspy.controller.schemaview;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import javax.management.modelmbean.XMLParseException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import net.objectof.actof.common.controller.IActofUIController;
import net.objectof.actof.common.controller.change.Change;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.schema.AttributeEntry;
import net.objectof.actof.common.controller.schema.SchemaController;
import net.objectof.actof.common.controller.schema.changes.AttributeCreationChange;
import net.objectof.actof.common.controller.schema.changes.AttributeRemovalChange;
import net.objectof.actof.common.controller.schema.changes.AttributeValueChange;
import net.objectof.actof.common.controller.schema.changes.SchemaInsertChange;
import net.objectof.actof.common.controller.schema.changes.SchemaRemovalChange;
import net.objectof.actof.common.controller.schema.changes.SchemaReplacedChange;
import net.objectof.actof.common.controller.schema.changes.SchemaStereotypeChange;
import net.objectof.actof.common.controller.schema.schemaentry.SchemaEntry;
import net.objectof.actof.common.icons.ActofIcons;
import net.objectof.actof.common.icons.ActofIcons.Icon;
import net.objectof.actof.common.icons.ActofIcons.Size;
import net.objectof.actof.common.util.AlphaNumericComparitor;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.schemaspy.SchemaSpyController;
import net.objectof.actof.schemaspy.controller.cards.ChildEntryCard;
import net.objectof.actof.schemaspy.controller.cards.SchemaSpyCard;
import net.objectof.actof.schemaspy.util.CodeGen;
import net.objectof.actof.widgets.card.Card;
import net.objectof.connector.Connector;
import net.objectof.connector.Connector.Initialize;

import org.controlsfx.control.BreadCrumbBar;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import org.xml.sax.SAXException;


public class SchemaViewController extends IActofUIController {

    @FXML
    private Button open, save, create, generate;

    @FXML
    private TreeTableView<SchemaEntry> tree;
    @FXML
    private TreeTableColumn<SchemaEntry, String> field;
    private BreadCrumbBar<SchemaEntry> breadcrumb;
    @FXML
    private HBox breadcrumbBox;

    @FXML
    private VBox cardpane;
    @FXML
    private ScrollPane cardscroller;
    @FXML
    private BorderPane editor;

    @FXML
    private TextField pkgdomain, pkgversion, pkgpath;

    @FXML
    private TitledPane namespacepane;

    SchemaSpyController schemaspy;
    Card addChildCard;

    TreeItem<SchemaEntry> root;
    Map<SchemaEntry, TreeItem<String>> schemaElements = new HashMap<>();

    public boolean modified = false;
    public File lastschemadir = null;

    @Override
    @FXML
    protected void initialize() throws SAXException, IOException, ParserConfigurationException, XMLParseException {

        cardscroller.setStyle("-fx-background-color:transparent;");
        cardscroller.setFitToWidth(true);

        createAddChildBar();

        breadcrumb = new BreadCrumbBar<>();
        breadcrumb.setFocusTraversable(false);
        breadcrumb.setAutoNavigationEnabled(false);
        breadcrumb.setOnCrumbAction(event -> {
            TreeItem<SchemaEntry> node = event.getSelectedCrumb();
            tree.getSelectionModel().select(node);
        });
        breadcrumbBox.getChildren().add(breadcrumb);
        breadcrumb.selectedCrumbProperty().bind(tree.getSelectionModel().selectedItemProperty());
        Callback<TreeItem<SchemaEntry>, Button> breadCrumbFactory = breadcrumb.getCrumbFactory();
        breadcrumb.setCrumbFactory(item -> {
            Button b = breadCrumbFactory.call(item);
            b.setText("");
            b.getStyleClass().add("bread-crumb-button");
            Label label = new Label();

            if (item.getValue() != null) {
                label.setText(item.getValue().getName());
            }

            b.setGraphic(label);
            label.setPadding(new Insets(3, 10, 3, 10));
            return b;
        });

        tree.getSelectionModel().selectedItemProperty().addListener(change -> doCardsLayout());
        doCardsLayout();

        field.setCellValueFactory(data -> {
            return new SimpleStringProperty(data.getValue().getValue().getName());
        });

        field.setCellFactory(data -> new TextFieldTreeTableCell<SchemaEntry, String>(new StringStringConverter()));

        field.setOnEditCommit(event -> {
            String name = event.getNewValue();
            SchemaEntry entry = event.getRowValue().getValue();
            entry.setName(name);
        });

        pkgdomain.setOnKeyReleased(evnet -> {
            if (schemaspy == null) { return; }
            SchemaController schema = schemaspy.getSchema();
            schema.setPackageDomain(pkgdomain.getText());
        });

        pkgversion.setOnKeyReleased(evnet -> {
            if (schemaspy == null) { return; }
            SchemaController schema = schemaspy.getSchema();
            schema.setPackageVersion(pkgversion.getText());
        });

        pkgpath.setOnKeyReleased(evnet -> {
            if (schemaspy == null) { return; }
            SchemaController schema = schemaspy.getSchema();
            schema.setPackagePath(pkgpath.getText());
        });

    }

    @Override
    public void ready() {
        getChangeBus().listen(this::onSchemaChange);

        getChangeBus().listen(change -> {
            change.when(SchemaReplacedChange.class, () -> modified = false);

            change.when(SchemaRemovalChange.class, () -> modified = true);
            change.when(SchemaInsertChange.class, () -> modified = true);
            change.when(SchemaStereotypeChange.class, () -> modified = true);
            change.when(AttributeCreationChange.class, () -> modified = true);
            change.when(AttributeRemovalChange.class, () -> modified = true);
            change.when(AttributeValueChange.class, () -> modified = true);

            save.setDisable(!modified);

        });

        save.setDisable(true);

    }

    public void setTopController(SchemaSpyController schemaspy) throws XMLParseException, SAXException, IOException,
            ParserConfigurationException {
        this.schemaspy = schemaspy;
        onNewSchema();
    }

    private void createAddChildBar() {
        addChildCard = new Card();
        addChildCard.setRadius(0);
        addChildCard.setPadding(new Insets(0));
        addChildCard.setTitle("Add Child");
        addChildCard.setShadowRadius(8);

        TextField addChildName = new TextField();
        addChildCard.setTitleContent(addChildName);

        Button addChildButton = new Button("", ActofIcons.getIconView(Icon.ADD, Size.BUTTON));
        addChildButton.getStyleClass().add("tool-bar-button");
        addChildCard.setDescription(addChildButton);

        Runnable create = () -> {
            String name = addChildName.getText();
            if (name.length() == 0) { return; }
            addChildName.setText("");
            addEntity(name);
        };

        addChildName.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                create.run();
            }
        });
        addChildButton.setOnAction(event -> create.run());

        editor.setTop(addChildCard);
    }

    private void onSchemaChange(Change change) {

        change.when(SchemaReplacedChange.class, this::onSchemaReplace);

        change.when(SchemaRemovalChange.class, removal -> {
            removeTreeElement(removal.getEntry(), tree.getRoot());
        });

        change.when(SchemaInsertChange.class, creation -> {
            SchemaEntry created = creation.getEntry();
            insertTreeElement(created, tree.getRoot());
        });

        change.when(SchemaStereotypeChange.class, this::doCardsLayout);
        change.when(SchemaInsertChange.class, this::doCardsLayout);
        change.when(SchemaRemovalChange.class, this::doCardsLayout);
        change.when(AttributeCreationChange.class, this::doCardsLayout);
        change.when(AttributeRemovalChange.class, this::doCardsLayout);

    }

    public void onNewSchema() throws SAXException, IOException, ParserConfigurationException, XMLParseException {
        schemaspy.newSchema();
    }

    public void onCreate() throws Exception {
        Connector connect = schemaspy.showConnect();
        if (connect == null) { return; }

        try {
            connect.createPackage(schemaspy.getSchema().getDocument(), Initialize.WHEN_EMPTY);

            Action action = Dialogs.create().title("Repository Created")
                    .masthead("This Repository can be viewed in RepoSpy").message("Open RepoSpy now?").showConfirm();

            if (action == Dialog.Actions.YES) {
                RepoSpyController repospy = new RepoSpyController(new Stage());
                repospy.initUI();
                repospy.connect(connect);
            }

        }
        catch (Exception e) {
            Dialogs.create().title("Repository Creation Failed")
                    .message("SchemaSpy failed to create the repository. Please check the parameters and try again.")
                    .showException(e);
        }

    }

    public void onGenerate() throws IOException, TransformerFactoryConfigurationError, TransformerException {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Generate Jar File");
        ExtensionFilter filter = new ExtensionFilter("Jar Files", "*.jar");
        chooser.setSelectedExtensionFilter(filter);
        File jarfile = chooser.showSaveDialog(schemaspy.primaryStage);
        if (jarfile == null) { return; }

        CodeGen.generate(schemaspy.getSchema(), jarfile);
    }

    public void onOpen() throws FileNotFoundException, SAXException, IOException, ParserConfigurationException,
            XMLParseException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Schema File");
        if (lastschemadir != null) {
            chooser.setInitialDirectory(lastschemadir);
        }
        ExtensionFilter filter = new ExtensionFilter("Schema Files", "*.xml");
        chooser.setSelectedExtensionFilter(filter);
        File file = chooser.showOpenDialog(schemaspy.primaryStage);
        if (file == null) { return; }
        lastschemadir = file.getParentFile();

        schemaspy.setSchema(file);
    }

    public void onSave() throws TransformerFactoryConfigurationError, TransformerException, IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Schema File");
        if (lastschemadir != null) {
            chooser.setInitialDirectory(lastschemadir);
        }
        ExtensionFilter filter = new ExtensionFilter("Schema Files", "xml");
        chooser.setSelectedExtensionFilter(filter);
        File file = chooser.showSaveDialog(schemaspy.primaryStage);
        if (file == null) { return; }

        lastschemadir = file.getParentFile();
        modified = false;

        String output = schemaspy.getSchema().toXML();
        Writer writer = new FileWriter(file);
        writer.write(output);
        writer.close();
    }

    private void addEntity(String name) {
        TreeItem<SchemaEntry> treeitem = tree.getSelectionModel().getSelectedItem();
        SchemaEntry entry = treeitem.getValue();
        entry.addChild(name);
    }

    private void onSchemaReplace() {
        populateTree();
        save.setDisable(false);
        create.setDisable(false);

        pkgdomain.setText(schemaspy.getSchema().getPackageDomain());
        pkgversion.setText(schemaspy.getSchema().getPackageVersion());
        pkgpath.setText(schemaspy.getSchema().getPackagePath());

        namespacepane.setExpanded(true);

    }

    private void populateTree() {
        root = new TreeItem<>(schemaspy.getSchema().getRoot());
        tree.setRoot(root);
        tree.setShowRoot(true);

        for (SchemaEntry entry : schemaspy.getSchema().getEntities()) {
            try {
                populateTreeItems(entry, root);
            }
            catch (XMLParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        sortTreeNode(root);
        tree.getSelectionModel().select(root);

    }

    /**
     * Iterate over all tree elements, removing them if they are the given entry
     * 
     * @param entry
     * @param parent
     */
    private void removeTreeElement(SchemaEntry entry, TreeItem<SchemaEntry> parent) {
        for (TreeItem<SchemaEntry> child : new ArrayList<>(parent.getChildren())) {
            removeTreeElement(entry, child); // recurse
            if (entry.equals(child.getValue())) {
                parent.getChildren().remove(child);
            }
        }
    }

    /**
     * This is a recursive function to insert a new SchemaEntry into the tree.
     * Its initial call should be with the entry to insert and the root node of
     * the tree.
     */
    private void insertTreeElement(SchemaEntry entry, TreeItem<SchemaEntry> parent) {
        if (entry.getParent().equals(parent.getValue())) {
            TreeItem<SchemaEntry> node = new TreeItem<>(entry);
            parent.getChildren().add(node);
            parent.setExpanded(true);
        }

        for (TreeItem<SchemaEntry> child : new ArrayList<>(parent.getChildren())) {
            insertTreeElement(entry, child); // recurse
        }
    }

    private void populateTreeItems(SchemaEntry entry, TreeItem<SchemaEntry> parent) throws XMLParseException {
        TreeItem<SchemaEntry> node = new TreeItem<>(entry);
        parent.getChildren().add(node);
        for (SchemaEntry child : entry.getChildren().values()) {
            populateTreeItems(child, node); // recurse
        }
        sortTreeNode(node);
    }

    private void sortTreeNode(TreeItem<SchemaEntry> parent) {

        AlphaNumericComparitor comp = new AlphaNumericComparitor();

        parent.getChildren().sort((ta, tb) -> {
            String sa = ta.getValue().getName();
            String sb = tb.getValue().getName();
            return comp.compare(sa, sb);
        });
    }

    private void doCardsLayout() {

        cardpane.getChildren().clear();

        TreeItem<SchemaEntry> selected = tree.getSelectionModel().getSelectedItem();
        if (selected == null) { return; }
        SchemaEntry entry = selected.getValue();

        // attribute cards for non-root entries
        if (!entry.isRoot()) {

            List<AttributeEntry> unhandledAttributes = entry.getAttributes();
            for (SchemaSpyCard card : SchemaSpyCard.allCards()) {
                if (!card.appliesTo(entry, unhandledAttributes)) {
                    continue;
                }
                card.initialize(schemaspy, entry, unhandledAttributes);
                unhandledAttributes.removeAll(card.attributes(unhandledAttributes));
                cardpane.getChildren().add(card);
            }

            if (selected.getChildren().size() > 0) {
                Separator sep = new Separator();
                sep.setPadding(new Insets(10, 25, 10, 25));
                cardpane.getChildren().add(sep);
            }
        }

        // child node cards
        for (TreeItem<SchemaEntry> subitem : selected.getChildren()) {
            Card childCard = new ChildEntryCard(tree, subitem);
            cardpane.getChildren().add(childCard);
        }

    }

    public static SchemaViewController load(ChangeController changes) throws IOException {
        return FXUtil.load(SchemaViewController.class, "SchemaView.fxml", changes);
    }

}

class StringStringConverter extends StringConverter<String> {

    @Override
    public String toString(String object) {
        return object;
    }

    @Override
    public String fromString(String string) {
        return string;
    }

}
