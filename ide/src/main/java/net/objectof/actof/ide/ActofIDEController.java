package net.objectof.actof.ide;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import net.objectof.actof.common.component.AbstractLoadedDisplay;
import net.objectof.actof.common.component.Display;
import net.objectof.actof.common.component.Resource;
import net.objectof.actof.common.component.ResourceDisplay;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.connectorui.SavedConnections;
import net.objectof.actof.ide.resource.ProjectResource;
import net.objectof.actof.repospy.resource.RepositoryResource;
import net.objectof.actof.schemaspy.resource.SchemaFileResource;


public class ActofIDEController extends AbstractLoadedDisplay {

    @FXML
    private TabPane tabs;
    @FXML
    private TreeView<Resource> tree;
    @FXML
    private HBox toolbar;

    private Map<Resource, Tab> resourceTabs = new HashMap<>();

    public static Display load(ChangeController changes) throws IOException {
        return FXUtil.loadDisplay(ActofIDEController.class, "ActofIDEController.fxml", changes);
    }

    public void setStage(Stage stage) {
        setStage(stage);
    }

    public void onShowDisplay() throws Exception {
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
                if (res.getDisplay() == null) { return; }

                Tab tab = getTab(res);
                System.out.println("tab = " + tab);
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
                toolbar.getChildren().clear();
                toolbar.getChildren().addAll(res.getDisplay().getToolbars());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void createTab(Resource res) throws Exception {

        ResourceDisplay display = res.getDisplay();
        display.setDisplayStage(getDisplayStage());
        display.setChangeBus(getChangeBus());
        display.setTop(false);
        display.initializeDisplay();

        display.setResource(res);
        display.loadResource();

        Tab tab = new Tab(res.getTitle(), display.getDisplayNode());
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
    public void initializeDisplay() throws Exception {}

    @Override
    public void onDisplayLoad() {}

}
