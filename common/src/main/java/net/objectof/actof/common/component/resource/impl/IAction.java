package net.objectof.actof.common.component.resource.impl;


import java.util.Optional;
import java.util.function.Supplier;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.objectof.actof.common.component.resource.Action;
import net.objectof.actof.common.component.resource.Resource;


public class IAction implements Action {

    private StringProperty title;
    private Supplier<Optional<Resource>> action;
    private BooleanProperty enabled = new SimpleBooleanProperty(true);

    public IAction(String title, Supplier<Optional<Resource>> action) {
        this.title = new SimpleStringProperty(title);
        this.action = action;
    }

    public IAction(String title, Runnable action) {
        this.title = new SimpleStringProperty(title);
        this.action = () -> {
            action.run();
            return Optional.empty();
        };
    }

    @Override
    public BooleanProperty getEnabledProperty() {
        return enabled;
    }

    @Override
    public Optional<Resource> perform() {
        return action.get();
    }

    @Override
    public StringProperty titleProperty() {
        return title;
    }

}
