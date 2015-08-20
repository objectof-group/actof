package net.objectof.actof.common.component.impl;


import javafx.scene.Node;
import net.objectof.actof.common.component.Panel;


public class INodePanel implements Panel {

    private Node node;
    private String title;
    private long timestamp = System.currentTimeMillis();

    public INodePanel(String title, Node node) {
        this.node = node;
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Node getDisplayNode() {
        return node;
    }

    @Override
    public void timestamp() {
        timestamp = System.currentTimeMillis();
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

}
