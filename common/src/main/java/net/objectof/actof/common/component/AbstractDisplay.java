package net.objectof.actof.common.component;


import javafx.stage.Stage;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.change.IChangeController;


public abstract class AbstractDisplay implements Display {

    private Stage displayStage;
    private ChangeController changeBus = new IChangeController();
    private boolean top = true;

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

}
