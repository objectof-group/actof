package net.objectof.actof.common.component.editor;


import javafx.collections.ObservableList;
import net.objectof.actof.common.component.display.Display;
import net.objectof.actof.common.component.feature.ChangeBusAware;
import net.objectof.actof.common.component.feature.DelayedConstruct;
import net.objectof.actof.common.component.feature.StageAware;
import net.objectof.actof.common.component.feature.Titled;
import net.objectof.actof.common.component.resource.Action;


public interface Editor extends Titled, ChangeBusAware, StageAware, DelayedConstruct {

    Display getDisplay();

    ObservableList<Action> getActions();

}
