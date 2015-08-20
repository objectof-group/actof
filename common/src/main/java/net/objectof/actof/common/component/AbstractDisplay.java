package net.objectof.actof.common.component;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.stage.Stage;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.change.IChangeController;


public abstract class AbstractDisplay implements Display {

    private Stage displayStage;
    private ChangeController changeBus = new IChangeController();
    private boolean top = true;

    private ObservableList<Node> toolbars = FXCollections.observableArrayList();
    private ObservableList<Node> panels = FXCollections.observableArrayList();
    private ObservableList<Display> subDisplays = FXCollections.observableArrayList();

    public Stage getDisplayStage() {
        return displayStage;
    }

    public void setDisplayStage(Stage displayStage) {
        this.displayStage = displayStage;
    }

    public ChangeController getChangeBus() {
        return changeBus;
    }

    public void setChangeBus(ChangeController bus) {
        this.changeBus = bus;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public boolean isTop() {
        return top;
    }

    @Override
    public ObservableList<Node> getToolbars() {
        return toolbars;
    }

    @Override
    public ObservableList<Node> getPanels() {
        return panels;
    }

    @Override
    public ObservableList<Display> getSubDisplays() {
        return subDisplays;
    }

}
