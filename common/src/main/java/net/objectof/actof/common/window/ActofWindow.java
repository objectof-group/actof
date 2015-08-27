package net.objectof.actof.common.window;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.objectof.actof.common.component.display.Display;
import net.objectof.actof.common.component.display.Panel;
import net.objectof.actof.common.component.editor.Editor;
import net.objectof.actof.common.component.feature.DelayedConstruct;
import net.objectof.actof.common.component.feature.FXLoaded;
import net.objectof.actof.common.component.feature.FXNoded;
import net.objectof.actof.common.component.feature.StageAware;
import net.objectof.actof.common.component.feature.Titled;
import net.objectof.actof.common.component.resource.Action;
import net.objectof.actof.common.component.resource.Resource;
import net.objectof.actof.common.icons.ActofIcons;
import net.objectof.actof.common.util.FXUtil;


public class ActofWindow implements Titled, FXNoded, FXLoaded, DelayedConstruct, StageAware {

    @FXML
    private BorderPane panel, topPane;
    @FXML
    private TabPane panels;
    @FXML
    private AnchorPane displayPanel;
    @FXML
    private HBox toolbar;
    @FXML
    private SplitPane splitPane;
    @FXML
    private MenuButton actionsButton;
    @FXML
    private Separator separator;

    private Node windowNode;
    private Stage stage = new Stage();

    private Editor editor;
    private Display display;

    private InvalidationListener panelsListener = (Observable change) -> layoutPanels();
    private InvalidationListener toolbarsListener = (Observable change) -> layoutToolbars();
    private InvalidationListener actionsListener = (Observable change) -> layoutActions();

    Map<Panel, Tab> panelTabs = new HashMap<>();

    public static ActofWindow load() throws IOException {
        return FXUtil.loadFX(ActofWindow.class, "ActofWindow.fxml");
    }

    public void setDisplayStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getDisplayStage() {
        return stage;
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public void setTitle(String title) {
        // TODO Auto-generated method stub

    }

    @Override
    public void construct() throws Exception {
        panels.setStyle("-fx-open-tab-animation: NONE; -fx-close-tab-animation: NONE;");
        SplitPane.setResizableWithParent(panel, false);
        actionsButton.setGraphic(ActofIcons.getCustomIcon(ActofWindow.class, "icons/menu.png"));
        actionsButton.getStyleClass().add("tool-bar-button");
        toolbar.getChildren().clear();

        topPane.sceneProperty().addListener(event -> fixTabBar());

    }

    private void updateDisplay() {

        layoutPanels();
        layoutToolbars();
        layoutActions();
        layoutDisplay();

    }

    private void layoutDisplay() {
        AnchorPane.setBottomAnchor(display.getFXNode(), 0d);
        AnchorPane.setTopAnchor(display.getFXNode(), 0d);
        AnchorPane.setLeftAnchor(display.getFXNode(), 0d);
        AnchorPane.setRightAnchor(display.getFXNode(), 0d);

        displayPanel.getChildren().clear();
        displayPanel.getChildren().add(display.getFXNode());
    }

    private void layoutActions() {

        actionsButton.getItems().clear();
        for (Action a : editor.getActions()) {
            MenuItem item = new MenuItem(a.getTitle());
            item.disableProperty().bind(a.getEnabledProperty().not());
            item.setOnAction(event -> {
                Optional<Resource> result = a.perform();
                if (result.isPresent()) {
                    editor.getResources().add(result.get());
                }
            });
            actionsButton.getItems().add(item);
        }

    }

    private void fixTabBar() {
        final StackPane header = (StackPane) panels.lookup(".tab-header-area");
        if (header != null) {
            if (panels.getTabs().size() == 1) header.setPrefHeight(0);
            else header.setPrefHeight(-1);
        }
    }

    private void layoutToolbars() {

        toolbar.getChildren().clear();

        if (editor == null) { return; }
        Display display = editor.getDisplay();
        if (display == null) { return; }

        toolbar.getChildren().addAll(display.getToolbars());
        if (editor.getActions().size() > 0) {
            toolbar.getChildren().addAll(separator, actionsButton);
        }
    }

    private void layoutPanels() {

        if (editor == null) {
            panels.getTabs().clear();
            panelTabs.clear();
            return;
        }

        if (display.getPanels().size() == 0) {
            topPane.setCenter(displayPanel);
            panels.getTabs().clear();
            panelTabs.clear();
        } else if (display.getPanels().size() == 1) {
            topPane.setCenter(splitPane);
            Panel p = display.getPanels().get(0);
            panel.setCenter(p.getFXNode());
        } else {
            topPane.setCenter(splitPane);
            panel.setCenter(panels);

            // in panels but not tabs
            List<Panel> toAdd = new ArrayList<>(display.getPanels());
            toAdd.removeAll(panelTabs.keySet());

            // in tabs but not in panels
            List<Panel> toRemove = new ArrayList<>(panelTabs.keySet());
            toRemove.removeAll(display.getPanels());

            // go through all panels for the display. If we haven't created a
            // tab for it yet, do so now.
            for (Panel panel : display.getPanels()) {
                if (panelTabs.containsKey(panel)) {
                    continue;
                }
                Tab tab = new Tab(panel.getTitle(), panel.getFXNode());
                panelTabs.put(panel, tab);

            }

            for (Panel p : toRemove) {
                Tab t = panelTabs.remove(p);
                panels.getTabs().remove(t);
            }

            for (Panel p : toAdd) {
                panels.getTabs().add(panelTabs.get(p));
            }
        }

        fixTabBar();

    }

    public Editor getEditor() {
        return editor;
    }

    public void setEditor(Editor editor) {

        if (this.editor != null) {
            this.editor.getActions().removeListener(actionsListener);
        }

        if (display != null) {
            display.getPanels().removeListener(panelsListener);
            display.getToolbars().removeListener(toolbarsListener);
        }

        this.editor = editor;
        if (editor == null) { return; }
        this.editor.getActions().addListener(actionsListener);

        this.display = editor.getDisplay();
        display.getPanels().addListener(panelsListener);
        display.getToolbars().addListener(toolbarsListener);

        updateDisplay();

        getDisplayStage().sizeToScene();

    }

    @Override
    public void setFXNode(Node node) {
        this.windowNode = node;
    }

    @Override
    public Node getFXNode() {
        return windowNode;
    }

    public void show() {
        Scene scene = new Scene((Parent) getFXNode());
        getDisplayStage().setScene(scene);
        getDisplayStage().show();
    }

}
