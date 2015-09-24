package net.objectof.actof.ide;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import net.objectof.actof.common.component.display.Display;
import net.objectof.actof.common.component.display.Panel;
import net.objectof.actof.common.component.display.impl.IPanel;
import net.objectof.actof.common.component.editor.Editor;
import net.objectof.actof.common.component.editor.impl.AbstractLoadedEditor;
import net.objectof.actof.common.component.resource.Action;
import net.objectof.actof.common.component.resource.Resource;
import net.objectof.actof.common.util.ActofSerialize;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.ide.resource.ProjectResource;
import net.objectof.actof.ide.resource.SerializableResource;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.resource.RepositoryResource;
import net.objectof.actof.schemaspy.SchemaSpyController;
import net.objectof.actof.schemaspy.resource.SchemaFileResource;
import net.objectof.connector.Connector;


public class ActofIDEController extends AbstractLoadedEditor implements Display {

    @FXML
    private BorderPane top;
    @FXML
    private TabPane tabs;
    @FXML
    private TreeView<Resource> tree;
    @FXML
    private HBox toolbar;
    @FXML
    private MenuButton newResource;

    private StringProperty title = new SimpleStringProperty("Actof IDE");

    private Map<Resource, Tab> resourceTabs = new HashMap<>();
    private List<Node> permanentToolbars = new ArrayList<>();
    private List<Panel> permanentPanels = new ArrayList<>();

    public static Editor load() throws IOException {
        return FXUtil.loadFX(ActofIDEController.class, "ActofIDEController.fxml");
    }

    public void onProjectOpen()
            throws FileNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(getStage());
        if (file == null) { return; }

        Scanner s = new Scanner(file);
        s.useDelimiter("\\Z");
        String contents = s.next();

        List<Object> objList = (List<Object>) ActofSerialize.deserialize(contents);

        getResources().clear();
        for (Object o : objList) {
            SerializableResource sr = ActofSerialize.convertObject(o, SerializableResource.class);
            Class<?> cls = getClass().forName(sr.cls);
            Resource res = (Resource) cls.newInstance();
            res.setTitle(sr.title);
            res.fromSerializableForm(sr.map);
            getResources().add(res);
        }

    }

    public void onProjectSave() throws IOException {

        FileChooser chooser = new FileChooser();
        File file = chooser.showSaveDialog(getStage());
        if (file == null) { return; }

        List<Object> serializedResources = getResources().stream()
                .map(r -> new SerializableResource(r.getTitle(), r.getClass().getCanonicalName(),
                        r.toSerializableForm()))
                .collect(Collectors.toList());

        String serialized = ActofSerialize.serialize(serializedResources);

        Writer writer = new FileWriter(file, false);
        writer.write(serialized);
        writer.close();

    }

    private void createTab(Resource res) {

        try {

            Editor editor = res.getEditor();
            editor.setStage(getStage());
            editor.setChangeBus(getChangeBus());
            editor.setResource(res);

            Display display = editor.getDisplay();

            Tab tab = new Tab(res.getTitle(), display.getFXRegion());
            tab.textProperty().bind(res.titleProperty());
            resourceTabs.put(res, tab);
            tabs.getTabs().add(tab);
            tabs.getSelectionModel().select(tab);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Tab getTab(Resource res) throws Exception {
        if (!resourceTabs.containsKey(res)) { return null; }
        return resourceTabs.get(res);
    }

    private Resource getResource(Tab tab) throws Exception {
        for (Resource res : resourceTabs.keySet()) {
            if (getTab(res).equals(tab)) { return res; }
        }
        return null;
    }

    @Override
    public void onFXLoad() {

        TreeItem<Resource> root = new TreeItem<Resource>(new ProjectResource());
        tree.setRoot(root);
        getResources().addListener((Observable change) -> updateTree());

        tree.getSelectionModel().selectedItemProperty().addListener(change -> {
            Resource res = tree.getSelectionModel().getSelectedItem().getValue();

            try {
                if (res.getEditor() == null) { return; }

                Tab tab = getTab(res);
                if (tab == null) {
                    createTab(res);
                } else {
                    tabs.getSelectionModel().select(tab);
                }
            }
            catch (Exception e1) {
                e1.printStackTrace();
            }

        });

        tree.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getButton() != MouseButton.SECONDARY) { return; }

            Resource res = tree.getSelectionModel().getSelectedItem().getValue();
            List<Action> actions = new ArrayList<>();
            actions.addAll(res.getActions());
            Editor editor = res.getEditor();
            if (editor != null) {
                actions.addAll(editor.getActions());
            }
            if (actions.size() == 0) { return; }

            ContextMenu menu = new ContextMenu();
            for (Action action : actions) {
                MenuItem item = new MenuItem(action.getTitle());
                item.setOnAction(event -> {
                    Optional<Resource> result = action.perform();
                    if (!result.isPresent()) { return; }
                    getResources().add(result.get());
                });
                menu.getItems().add(item);
            }
            tree.setContextMenu(menu);
            // menu.show(tree, e.getScreenX(), e.getScreenY());
        });

        tabs.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            try {
                if (n == null) { return; }
                Resource res = getResource(n);

                Editor editor = res.getEditor();
                getActions().setAll(editor.getActions());

                getToolbars().clear();
                getToolbars().addAll(permanentToolbars);
                getToolbars().addAll(editor.getToolbars());

                List<Panel> panelList = new ArrayList<>();
                panelList.addAll(permanentPanels);
                panelList.addAll(editor.getPanels());
                getPanels().setAll(panelList);

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });

        top.getChildren().removeAll(toolbar, tree, tabs);
        setFXRegion(tabs);

        permanentToolbars.addAll(toolbar.getChildren());
        getToolbars().addAll(permanentToolbars);
        toolbar.getChildren().clear();

        Panel projectPanel = new IPanel("Project", tree);
        projectPanel.setDismissible(false);
        permanentPanels.add(projectPanel);
        getPanels().addAll(permanentPanels);

    }

    private void updateTree() {
        TreeItem<Resource> root = tree.getRoot();
        root.getChildren().clear();
        getResources().stream().filter(r -> !r.isTransient()).forEachOrdered(res -> {
            TreeItem<Resource> item = new TreeItem<Resource>(res);
            root.getChildren().add(item);
        });
    }

    public void onAddSchema() {
        File file = SchemaSpyController.chooseSchemaFile(null, getStage());
        if (file == null) { return; }
        SchemaFileResource schema = new SchemaFileResource();
        schema.setSchemaFile(file);
        getResources().add(schema);

    }

    public void onAddRepository() throws IOException {
        Connector conn = RepoSpyController.showConnect(getStage());
        RepositoryResource repo = new RepositoryResource();
        repo.setConnector(conn);
        getResources().add(repo);
    }

    @Override
    public Display getDisplay() {
        return this;
    }

    @Override
    protected void onResourceAdded(Resource res) throws Exception {
        if (res.isTransient()) {
            createTab(res);
        }
    }

    @Override
    public StringProperty titleProperty() {
        return title;
    }

}
