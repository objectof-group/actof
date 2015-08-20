package net.objectof.actof.porter.ui.porter;


import javafx.scene.Node;
import net.objectof.actof.common.component.Display;
import net.objectof.actof.common.component.impl.AbstractDisplay;


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
    public void initializeDisplay() throws Exception {
        controller = PorterUIController.load(getChangeBus());
        controller.setDisplayStage(getDisplayStage());
        controller.initializeDisplay();
    }

    @Override
    public void onShowDisplay() throws Exception {
        controller.onShowDisplay();
    }

}
