package net.objectof.actof.common.component.display;


import javafx.collections.ObservableList;
import javafx.scene.Node;
import net.objectof.actof.common.component.feature.ChangeBusAware;
import net.objectof.actof.common.component.feature.Noded;
import net.objectof.actof.common.component.feature.StageAware;
import net.objectof.actof.common.component.feature.Titled;


public interface Display extends Titled, Noded, StageAware, ChangeBusAware {

    ObservableList<Node> getToolbars();

    ObservableList<Panel> getPanels();

    ObservableList<Display> getSubDisplays();

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
