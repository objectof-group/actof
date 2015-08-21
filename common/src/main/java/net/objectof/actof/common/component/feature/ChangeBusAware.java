package net.objectof.actof.common.component.feature;


import net.objectof.actof.common.controller.change.ChangeController;


public interface ChangeBusAware {

    ChangeController getChangeBus();

    void setChangeBus(ChangeController bus);

}
