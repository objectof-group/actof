package net.objectof.actof.web.app.change;


import net.objectof.actof.common.controller.change.Change;

import org.eclipse.jetty.webapp.WebAppContext;


public class HandlerChange extends Change {

    private WebAppContext handler;

    public HandlerChange(WebAppContext handler) {
        super();
        this.handler = handler;
    }

    public WebAppContext getHandler() {
        return handler;
    }

}
