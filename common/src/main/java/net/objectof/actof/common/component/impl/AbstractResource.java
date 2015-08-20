package net.objectof.actof.common.component.impl;

import net.objectof.actof.common.component.Resource;
import net.objectof.actof.common.component.ResourceDisplay;

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
