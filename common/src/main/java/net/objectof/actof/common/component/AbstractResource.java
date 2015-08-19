package net.objectof.actof.common.component;


public abstract class AbstractResource implements Resource {

    protected ResourceDisplay display;

    @Override
    public ResourceDisplay getDisplay() throws Exception {
        if (display == null) {
            display = createDisplay();
        }
        return display;
    }

}
