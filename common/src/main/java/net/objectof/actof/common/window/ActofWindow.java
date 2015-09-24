package net.objectof.actof.common.window;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.objectof.actof.common.component.editor.Editor;
import net.objectof.actof.common.component.editor.impl.ResourceView;
import net.objectof.actof.common.component.feature.ResourceProperty;
import net.objectof.actof.common.component.resource.Resource;
import net.objectof.actof.common.component.resource.impl.TransientResource;


public class ActofWindow implements ResourceProperty {

    private ObjectProperty<Resource> resourceProperty = new SimpleObjectProperty<Resource>(null);
    private ResourceView editorPane;
    private Stage stage;

    public ActofWindow(Editor editor) throws Exception {
        this(new TransientResource(editor));
    }

    public ActofWindow(Stage stage, Editor editor) throws Exception {
        this(stage, new TransientResource(editor));
    }

    public ActofWindow(Resource resource) throws Exception {
        this(new Stage(), resource);
    }

    public ActofWindow(Stage stage, Resource resource) throws Exception {
        this.stage = stage;

        resource.getEditor().setStage(stage);

        editorPane = ResourceView.load();
        editorPane.setStage(stage);
        editorPane.setResource(resource);

        setResource(resource);

    }

    public void show() {
        Scene scene = new Scene(editorPane.getFXRegion());
        stage.setScene(scene);
        stage.show();
    }

    public void setSize(int width, int height) {
        editorPane.getFXRegion().setPrefHeight(height);
        editorPane.getFXRegion().setPrefWidth(width);
    }

    public ResourceView getEditorPane() {
        return editorPane;
    }

    @Override
    public ObjectProperty<Resource> resourceProperty() {
        return resourceProperty;
    }

}
