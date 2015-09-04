package net.objectof.actof.common.component.editor;


import javafx.collections.ObservableList;
import javafx.scene.Node;
import net.objectof.actof.common.component.display.Display;
import net.objectof.actof.common.component.display.Panel;
import net.objectof.actof.common.component.feature.ChangeBusAware;
import net.objectof.actof.common.component.feature.Dismissible;
import net.objectof.actof.common.component.feature.ResourceProperty;
import net.objectof.actof.common.component.feature.StageProperty;
import net.objectof.actof.common.component.feature.Titled;
import net.objectof.actof.common.component.resource.Action;
import net.objectof.actof.common.component.resource.Resource;


public interface Editor extends Titled, ChangeBusAware, StageProperty, Dismissible, ResourceProperty {

    Display getDisplay();

    ObservableList<Action> getActions();

    ObservableList<Resource> getResources();

    ObservableList<Node> getToolbars();

    ObservableList<Panel> getPanels();

}
