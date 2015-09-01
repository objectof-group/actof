package net.objectof.actof.porter.ui.porter;


import javafx.scene.layout.Region;
import net.objectof.actof.common.component.display.Display;
import net.objectof.actof.common.component.display.impl.AbstractDisplay;


public class PorterUIDisplay extends AbstractDisplay {

    private Display controller;

    @Override
    public Region getFXRegion() {
        return controller.getFXRegion();
    }

    @Override
    public String getTitle() {
        return controller.getTitle();
    }

    @Override
    public void construct() throws Exception {
        controller = PorterUIController.load();
        controller.setChangeBus(getChangeBus());
        controller.setDisplayStage(getDisplayStage());
        controller.construct();
    }

}
