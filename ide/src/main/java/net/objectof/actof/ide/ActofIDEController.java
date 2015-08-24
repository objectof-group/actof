package net.objectof.actof.ide;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import net.objectof.actof.common.component.display.Display;
import net.objectof.actof.common.component.display.Panel;
import net.objectof.actof.common.component.display.impl.AbstractLoadedDisplay;
import net.objectof.actof.common.component.display.impl.INodePanel;
import net.objectof.actof.common.component.editor.Editor;
import net.objectof.actof.common.component.editor.ResourceEditor;
import net.objectof.actof.common.component.resource.Action;
import net.objectof.actof.common.component.resource.Resource;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.connectorui.SavedConnections;
import net.objectof.actof.ide.resource.ProjectResource;
import net.objectof.actof.repospy.resource.RepositoryResource;
import net.objectof.actof.schemaspy.resource.SchemaFileResource;


public class ActofIDEController extends AbstractLoadedDisplay implements Editor {

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

    ObservableList<Action> actions = FXCollections.observableArrayList();

    private Map<Resource, Tab> resourceTabs = new HashMap<>();
    private List<Node> permanentToolbars = new ArrayList<>();
    private List<Panel> permanentPanels = new ArrayList<>();

    public static Editor load() throws IOException {
        return FXUtil.loadFX(ActofIDEController.class, "ActofIDEController.fxml");
    }

    public void setStage(Stage stage) {
        setStage(stage);
    }

    public void onProjectOpen() {}

    public void onProjectSave() {}

    private void createTab(Resource res) throws Exception {

        ResourceEditor editor = res.getEditor();
        editor.setDisplayStage(getDisplayStage());
        editor.setChangeBus(getChangeBus());
        editor.setForResource(true);
        editor.construct();

        editor.setResource(res);
        editor.loadResource();

        Display display = editor.getDisplay();

        Tab tab = new Tab(res.getTitle(), display.getFXNode());
        resourceTabs.put(res, tab);
        tabs.getTabs().add(tab);
        tabs.getSelectionModel().select(tab);

        display.onShowDisplay();
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
    public String getTitle() {
        return "Actof IDE";
    }

    @Override
    public void construct() throws Exception {

        TreeItem<Resource> root = new TreeItem<Resource>(new ProjectResource());
        tree.setRoot(root);

        SchemaFileResource sch;

        sch = new SchemaFileResource();
        sch.setSchemaFile(new File("/home/nathaniel/Desktop/REALM Misc/GettingStarted/schema.xml"));
        root.getChildren().add(new TreeItem<Resource>(sch));

        sch = new SchemaFileResource();
        sch.setSchemaFile(new File("/home/nathaniel/Desktop/repo/realm.xml"));
        root.getChildren().add(new TreeItem<Resource>(sch));

        RepositoryResource repo;

        repo = new RepositoryResource();
        repo.setConnector(SavedConnections.getLastConnector());
        root.getChildren().add(new TreeItem<Resource>(repo));

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

        tabs.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            try {
                if (n == null) { return; }
                Resource res = getResource(n);

                Editor editor = res.getEditor();
                actions.setAll(editor.getActions());

                Display display = editor.getDisplay();
                getToolbars().clear();
                getToolbars().addAll(permanentToolbars);
                getToolbars().addAll(display.getToolbars());

                List<Panel> panelList = new ArrayList<>();
                panelList.addAll(permanentPanels);
                panelList.addAll(display.getPanels());
                getPanels().setAll(panelList);

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });

        top.getChildren().removeAll(toolbar, tree, tabs);
        setFXNode(tabs);

        permanentToolbars.addAll(toolbar.getChildren());
        getToolbars().addAll(permanentToolbars);
        toolbar.getChildren().clear();

        permanentPanels.add(new INodePanel("Project", tree));
        getPanels().addAll(permanentPanels);

    }

    @Override
    public Display getDisplay() {
        return this;
    }

    @Override
    public ObservableList<Action> getActions() {
        return actions;
    }

}
