package net.objectof.actof.common.component.display.impl;

import net.objectof.actof.common.component.display.ResourceDisplay;
import net.objectof.actof.common.component.resource.Resource;

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
