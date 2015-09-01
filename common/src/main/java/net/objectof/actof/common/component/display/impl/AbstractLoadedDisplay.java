package net.objectof.actof.common.component.display.impl;


import javafx.scene.layout.Region;
import net.objectof.actof.common.component.feature.FXLoaded;


public abstract class AbstractLoadedDisplay extends AbstractDisplay implements FXLoaded {

    private Region displayNode;

    @Override
    public Region getFXRegion() {
        return displayNode;
    }

    @Override
    public void setFXRegion(Region node) {
        this.displayNode = node;
    }

}
