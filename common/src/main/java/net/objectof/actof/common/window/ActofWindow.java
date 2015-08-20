package net.objectof.actof.common.window;


import java.io.IOException;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import net.objectof.actof.common.component.AbstractLoadedDisplay;
import net.objectof.actof.common.component.Display;
import net.objectof.actof.common.util.FXUtil;


public class ActofWindow extends AbstractLoadedDisplay {

    @FXML
    private Accordion panels;
    @FXML
    private AnchorPane displayPanel;
    @FXML
    private HBox toolbar;
    @FXML
    private SplitPane splitPane;

    private Display display;

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

        getSubDisplays().addListener((Observable change) -> {
            toolbar.getChildren().clear();
            splitPane.getItems().clear();

            if (display != null) {
                Bindings.unbindContent(toolbar.getChildren(), display.getToolbars());
            }

            if (getSubDisplays().size() == 0) {
                display = null;
            } else {

                display = getSubDisplays().get(0);
                Bindings.bindContent(toolbar.getChildren(), display.getToolbars());

                AnchorPane.setBottomAnchor(display.getDisplayNode(), 0d);
                AnchorPane.setTopAnchor(display.getDisplayNode(), 0d);
                AnchorPane.setLeftAnchor(display.getDisplayNode(), 0d);
                AnchorPane.setRightAnchor(display.getDisplayNode(), 0d);

                System.out.println(display.getPanels());
                if (display.getPanels().size() == 0) {
                    // nothing to do
                } else if (display.getPanels().size() == 1) {
                    Node n = display.getPanels().get(0);
                    SplitPane.setResizableWithParent(n, false);
                    splitPane.getItems().add(n);
                } else {
                    for (Node panel : display.getPanels()) {
                        TitledPane title = new TitledPane("title", panel);
                        panels.getPanes().add(title);
                    }
                    splitPane.getItems().add(panels);
                }

                displayPanel.getChildren().clear();
                displayPanel.getChildren().add(display.getDisplayNode());
                splitPane.getItems().add(displayPanel);

                if (display.getPanels().size() > 0) {
                    splitPane.setDividerPositions(0.25);
                }

            }
        });
    }

}
