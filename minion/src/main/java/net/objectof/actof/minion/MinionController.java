package net.objectof.actof.minion;


import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;


public class MinionController {

    @FXML
    private TabPane tabs;

    @FXML
    private void initialize() {

    }

    public void addTab(Node node, String title) {

        Tab tab = new Tab(title);
        tab.setContent(node);
        tabs.getTabs().add(tab);

    }

    public void selectNextTab() {
        tabs.getSelectionModel().selectNext();
    }

}
