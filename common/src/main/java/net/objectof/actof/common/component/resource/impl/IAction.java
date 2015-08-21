package net.objectof.actof.common.component.resource.impl;


import net.objectof.actof.common.component.resource.Action;


public class IAction implements Action {

    private String title;
    private Runnable runnable;

    public IAction(String title, Runnable runnable) {
        this.title = title;
        this.runnable = runnable;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void run() {
        runnable.run();
    }

}
