package net.objectof.actof.common.component.editor.impl;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Region;
import net.objectof.actof.common.component.display.Panel;
import net.objectof.actof.common.component.resource.Resource;


public class ResourcePanel implements Panel {

    private Resource resource;
    private ResourceView view;
    private long timestamp = System.currentTimeMillis();

    private BooleanProperty dismissible = new SimpleBooleanProperty(true);
    private BooleanProperty dismissed = new SimpleBooleanProperty(false);

    public ResourcePanel(Resource resource) throws Exception {
        this.resource = resource;
        view = ResourceView.load();
        view.setStage(resource.getEditor().getStage());
        view.setResource(resource);
        dismissedProperty().addListener(e -> {
            resource.getEditor().dismiss();
        });
    }

    @Override
    public StringProperty titleProperty() {
        return resource.getEditor().titleProperty();
    }

    @Override
    public Region getFXRegion() {
        return view.getFXRegion();
    }

    @Override
    public void timestamp() {
        timestamp = System.currentTimeMillis();
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public BooleanProperty dismissibleProperty() {
        return dismissible;
    }

    @Override
    public BooleanProperty dismissedProperty() {
        return dismissed;
    }

}
