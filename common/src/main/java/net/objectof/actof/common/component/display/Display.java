package net.objectof.actof.common.component.display;


import net.objectof.actof.common.component.feature.ChangeBusAware;
import net.objectof.actof.common.component.feature.DelayedConstruct;
import net.objectof.actof.common.component.feature.Dismissible;
import net.objectof.actof.common.component.feature.FXRegion;
import net.objectof.actof.common.component.feature.StageAware;
import net.objectof.actof.common.component.feature.Titled;


public interface Display extends Titled, FXRegion, StageAware, ChangeBusAware, DelayedConstruct, Dismissible {

}
