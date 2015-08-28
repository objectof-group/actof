package net.objectof.actof.common.component.display;


import javafx.collections.ObservableList;
import javafx.scene.Node;
import net.objectof.actof.common.component.feature.ChangeBusAware;
import net.objectof.actof.common.component.feature.DelayedConstruct;
import net.objectof.actof.common.component.feature.FXRegion;
import net.objectof.actof.common.component.feature.StageAware;
import net.objectof.actof.common.component.feature.Titled;


public interface Display extends Titled, FXRegion, StageAware, ChangeBusAware, DelayedConstruct {

    ObservableList<Node> getToolbars();

    ObservableList<Panel> getPanels();

}
