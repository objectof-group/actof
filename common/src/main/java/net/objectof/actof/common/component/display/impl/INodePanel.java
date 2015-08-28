package net.objectof.actof.common.component.display.impl;


import javafx.scene.layout.Region;
import net.objectof.actof.common.component.display.Panel;


public class INodePanel implements Panel {

    private Region region;
    private String title;
    private long timestamp = System.currentTimeMillis();

    public INodePanel(String title, Region region) {
        this.region = region;
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Region getFXRegion() {
        return region;
    }

    @Override
    public void timestamp() {
        timestamp = System.currentTimeMillis();
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

}
