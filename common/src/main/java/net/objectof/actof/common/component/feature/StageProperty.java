package net.objectof.actof.common.component.feature;


import javafx.beans.property.ObjectProperty;
import javafx.stage.Stage;


public interface StageProperty {

    public ObjectProperty<Stage> stageProperty();

    default void setStage(Stage stage) {
        stageProperty().set(stage);
    }

    default Stage getStage() {
        return stageProperty().get();
    }

}
