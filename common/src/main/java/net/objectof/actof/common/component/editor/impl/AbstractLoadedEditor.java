package net.objectof.actof.common.component.editor.impl;


import javafx.scene.layout.Region;
import net.objectof.actof.common.component.feature.FXLoaded;
import net.objectof.actof.common.component.feature.FXRegion;


public abstract class AbstractLoadedEditor extends AbstractEditor implements FXRegion, FXLoaded {

    private Region region;

    @Override
    public void setFXRegion(Region region) {
        this.region = region;
    }

    @Override
    public Region getFXRegion() {
        return region;
    }

}
