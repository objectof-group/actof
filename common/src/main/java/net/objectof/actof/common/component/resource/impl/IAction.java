package net.objectof.actof.common.component.resource.impl;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import net.objectof.actof.common.component.resource.Action;


public class IAction implements Action {

    private String title;
    private Runnable runnable;
    private BooleanProperty enabled = new SimpleBooleanProperty(true);

    public IAction(String title, Runnable runnable) {
        this.title = title;
        this.runnable = runnable;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void run() {
        runnable.run();
    }

    @Override
    public BooleanProperty getEnabledProperty() {
        return enabled;
    }

}
