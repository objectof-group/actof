package net.objectof.actof.porter.ui.porter;


import javafx.scene.Node;
import net.objectof.actof.common.component.AbstractDisplay;
import net.objectof.actof.common.component.Display;


public class PorterUIDisplay extends AbstractDisplay {

    private Display controller;

    @Override
    public Node getDisplayNode() {
        return controller.getDisplayNode();
    }

    @Override
    public String getTitle() {
        return controller.getTitle();
    }

    @Override
    public void initialize() throws Exception {
        controller = PorterUIController.load(getChangeBus());
        controller.setDisplayStage(getDisplayStage());
        controller.initialize();
    }

    @Override
    public void onShow() throws Exception {
        controller.onShow();
    }

}
