package net.objectof.actof.common.component.resource.impl;


import java.util.Optional;
import java.util.function.Supplier;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import net.objectof.actof.common.component.resource.Action;
import net.objectof.actof.common.component.resource.Resource;


public class IAction implements Action {

    private String title;
    private Supplier<Optional<Resource>> action;
    private BooleanProperty enabled = new SimpleBooleanProperty(true);

    public IAction(String title, Supplier<Optional<Resource>> action) {
        this.title = title;
        this.action = action;
    }

    public IAction(String title, Runnable action) {
        this.title = title;
        this.action = () -> {
            action.run();
            return Optional.empty();
        };
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public BooleanProperty getEnabledProperty() {
        return enabled;
    }

    @Override
    public Optional<Resource> perform() {
        return action.get();
    }

}
