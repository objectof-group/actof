package net.objectof.actof.common.window;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import net.objectof.actof.common.component.AbstractLoadedDisplay;
import net.objectof.actof.common.component.Display;
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
    private InvalidationListener panelsListener = (Observable change) -> {

        if (display == null) {
            panels.getTabs().clear();
            return;
        }
        if (display.getPanels().size() == 0) {
            panels.getTabs().clear();
        } else {
            layoutPanels();
        }

    };

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

        List<Tab> newtabs = new ArrayList<>();

        for (Node panel : display.getPanels()) {
            Tab tab = new Tab("title", panel);
            newtabs.add(tab);
        }
        panels.getTabs().setAll(newtabs);
        panel.setCenter(panels);
    }

}
