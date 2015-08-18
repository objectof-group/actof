package net.objectof.actof.common.component;


import javafx.scene.Node;
import javafx.stage.Stage;
import net.objectof.actof.common.controller.change.ChangeController;


public interface Display {

    Node getDisplayNode();

    String getTitle();

    Resource getResource();

    void setResource(Resource resource);

    Stage getDisplayStage();

    void setDisplayStage(Stage stage);

    ChangeController getChangeBus();

    void setChangeBus(ChangeController bus);

    void setTop(boolean top);

    boolean isTop();

    /**
     * To be called after properties such as the stage and change bus have been
     * set.
     * 
     * @throws Exception
     */
    void initialize() throws Exception;

    /**
     * To be called after the component has been shown on screen for the first
     * time.
     * 
     * @throws Exception
     */
    void onShow() throws Exception;

}
