package net.objectof.actof.common.window;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import net.objectof.actof.common.component.display.Display;
import net.objectof.actof.common.component.display.Panel;
import net.objectof.actof.common.component.editor.Editor;
import net.objectof.actof.common.component.feature.DelayedConstruct;
import net.objectof.actof.common.component.feature.FXLoaded;
import net.objectof.actof.common.component.feature.FXNoded;
import net.objectof.actof.common.component.feature.StageAware;
import net.objectof.actof.common.component.feature.Titled;
import net.objectof.actof.common.icons.ActofIcons;
import net.objectof.actof.common.util.FXUtil;


public class ActofWindow implements Titled, FXNoded, FXLoaded, DelayedConstruct, StageAware {

    @FXML
    private BorderPane panel;
    @FXML
    private TabPane panels;
    @FXML
    private AnchorPane displayPanel;
    @FXML
    private HBox toolbar;
    @FXML
    private SplitPane splitPane;

    private Node windowNode;
    private Stage stage;

    private Editor editor;
    private InvalidationListener panelsListener = (Observable change) -> layoutPanels();
    private InvalidationListener toolbarsListener = (Observable change) -> layoutToolbars();
    private MenuButton actionsButton;

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
    public void construct() throws Exception {
        panels.setStyle("-fx-open-tab-animation: NONE; -fx-close-tab-animation: NONE;");
        SplitPane.setResizableWithParent(panel, false);
        actionsButton = new MenuButton("", ActofIcons.getCustomIcon(ActofWindow.class, "icons/menu.png"));
    }

    private void updateDisplay() {

        toolbar.getChildren().clear();

        if (editor == null) { return; }

        Display display = editor.getDisplay();

        if (display != null) {
            display.getPanels().removeListener(panelsListener);
            display.getToolbars().removeListener(toolbarsListener);
        }

        display.getPanels().addListener(panelsListener);
        display.getToolbars().addListener(toolbarsListener);
        layoutPanels();
        layoutToolbars();

        AnchorPane.setBottomAnchor(display.getFXNode(), 0d);
        AnchorPane.setTopAnchor(display.getFXNode(), 0d);
        AnchorPane.setLeftAnchor(display.getFXNode(), 0d);
        AnchorPane.setRightAnchor(display.getFXNode(), 0d);

        displayPanel.getChildren().clear();
        displayPanel.getChildren().add(display.getFXNode());

    }

    private void layoutToolbars() {

        toolbar.getChildren().clear();

        if (editor == null) { return; }
        Display display = editor.getDisplay();
        if (display == null) { return; }

        toolbar.getChildren().addAll(display.getToolbars());
        toolbar.getChildren().add(actionsButton);
    }

    private void layoutPanels() {

        if (editor == null) {
            panels.getTabs().clear();
            panelTabs.clear();
            return;
        }

        Display display = editor.getDisplay();

        if (display.getPanels().size() == 0) {
            panels.getTabs().clear();
            panelTabs.clear();
        } else if (display.getPanels().size() == 1) {
            Panel p = display.getPanels().get(0);
            panel.setCenter(p.getFXNode());
        } else {

            if (panel.getCenter() != panels) {
                panel.setCenter(panels);
            }

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

    }

    public Editor getEditor() {
        return editor;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
        updateDisplay();

    }

    @Override
    public void setFXNode(Node node) {
        this.windowNode = node;
    }

    @Override
    public Node getFXNode() {
        return windowNode;
    }

}
