package net.objectof.actof.common.component;


import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.stage.Stage;
import net.objectof.actof.common.controller.change.ChangeController;


public interface Display extends Titled, Noded {

    Stage getDisplayStage();

    void setDisplayStage(Stage stage);

    ObservableList<Node> getToolbars();

    ObservableList<Panel> getPanels();

    ObservableList<Display> getSubDisplays();

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
    void initializeDisplay() throws Exception;

    /**
     * To be called after the component has been shown on screen for the first
     * time.
     * 
     * @throws Exception
     */
    default void onShowDisplay() throws Exception {
        for (Display d : getSubDisplays()) {
            d.onShowDisplay();
        }
    }

    default void copySettings(Display other) {
        setChangeBus(other.getChangeBus());
        setTop(other.isTop());
        setDisplayStage(other.getDisplayStage());
    }

}
