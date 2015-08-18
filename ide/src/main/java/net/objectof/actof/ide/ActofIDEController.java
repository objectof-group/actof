package net.objectof.actof.ide;


import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import net.objectof.actof.common.component.AbstractLoadedDisplay;
import net.objectof.actof.common.component.Display;
import net.objectof.actof.common.component.Resource;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.porter.ui.porter.PorterUIDisplay;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.schemaspy.SchemaSpyController;


public class ActofIDEController extends AbstractLoadedDisplay {

    @FXML
    private TabPane tabs;
    @FXML
    private TreeView<Resource> tree;

    public static Display load(ChangeController changes) throws IOException {
        return FXUtil.loadDisplay(ActofIDEController.class, "ActofIDEController.fxml", changes);
    }

    public void setStage(Stage stage) {
        setStage(stage);
    }

    public void onShow() throws Exception {

        createTab(new RepoSpyController());
        createTab(new SchemaSpyController());
        createTab(new PorterUIDisplay());

    }

    private void createTab(Display display) throws Exception {
        display.setDisplayStage(getDisplayStage());
        display.setChangeBus(getChangeBus());
        display.setTop(false);
        display.initialize();

        Tab tab = new Tab(display.getTitle(), display.getDisplayNode());
        tabs.getTabs().add(tab);

        display.onShow();
    }

    @Override
    public String getTitle() {
        return "Actof IDE";
    }

    @Override
    public void initialize() throws Exception {}

    @Override
    public void onDisplayLoad() {}

}
