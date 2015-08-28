package net.objectof.actof.common.component.editor.impl;


import javafx.scene.layout.Region;
import net.objectof.actof.common.component.display.Panel;
import net.objectof.actof.common.component.editor.Editor;


public class EditorPanel implements Panel {

    private Editor editor;
    private EditorPane pane;
    private long timestamp = System.currentTimeMillis();

    public EditorPanel(Editor editor) throws Exception {
        this.editor = editor;
        pane = EditorPane.load();
        pane.setDisplayStage(editor.getDisplayStage());
        pane.construct();
        pane.setEditor(editor);
    }

    @Override
    public String getTitle() {
        return pane.getTitle();
    }

    @Override
    public void setTitle(String title) {
        pane.setTitle(title);
    }

    @Override
    public Region getFXRegion() {
        return pane.getFXRegion();
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
