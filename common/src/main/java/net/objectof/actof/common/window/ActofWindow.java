package net.objectof.actof.common.window;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import net.objectof.actof.common.component.Display;
import net.objectof.actof.common.component.Panel;
import net.objectof.actof.common.component.impl.AbstractLoadedDisplay;
import net.objectof.actof.common.util.FXUtil;


public class ActofWindow extends AbstractLoadedDisplay {

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

    private Display display;
    private InvalidationListener panelsListener = (Observable change) -> layoutPanels();

    Map<Panel, Tab> panelTabs = new HashMap<>();

    public static ActofWindow load() throws IOException {
        return FXUtil.loadDisplay(ActofWindow.class, "ActofWindow.fxml", null);
    }

    public void setStage(Stage stage) {
        setStage(stage);
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public void initializeDisplay() throws Exception {

        panels.setStyle("-fx-open-tab-animation: NONE; -fx-close-tab-animation: NONE;");

        SplitPane.setResizableWithParent(panel, false);

        getSubDisplays().addListener((Observable change) -> {
            toolbar.getChildren().clear();

            if (display != null) {
                Bindings.unbindContent(toolbar.getChildren(), display.getToolbars());
                display.getPanels().removeListener(panelsListener);
            }

            if (getSubDisplays().size() == 0) {
                display = null;
                panelsListener.invalidated(null);
            } else {

                display = getSubDisplays().get(0);
                Bindings.bindContent(toolbar.getChildren(), display.getToolbars());
                display.getPanels().addListener(panelsListener);
                layoutPanels();

                AnchorPane.setBottomAnchor(display.getDisplayNode(), 0d);
                AnchorPane.setTopAnchor(display.getDisplayNode(), 0d);
                AnchorPane.setLeftAnchor(display.getDisplayNode(), 0d);
                AnchorPane.setRightAnchor(display.getDisplayNode(), 0d);

                displayPanel.getChildren().clear();
                displayPanel.getChildren().add(display.getDisplayNode());

            }
        });

    }

    private void layoutPanels() {

        if (display == null) {
            panels.getTabs().clear();
            panelTabs.clear();
            return;
        }
        if (display.getPanels().size() == 0) {
            panels.getTabs().clear();
            panelTabs.clear();
        } else if (display.getPanels().size() == 1) {
            Panel p = display.getPanels().get(0);
            panel.setCenter(p.getDisplayNode());
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
                Tab tab = new Tab(panel.getTitle(), panel.getDisplayNode());
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

}
