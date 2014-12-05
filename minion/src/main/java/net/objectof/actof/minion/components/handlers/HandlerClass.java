package net.objectof.actof.minion.components.handlers;


import java.io.File;

import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.IHandler;


public class HandlerClass {

    private Class<IHandler<? extends HttpRequest>> cls;
    private String name;
    private File icon;

    public HandlerClass(Class<IHandler<? extends HttpRequest>> cls, String name, File icon) {
        this.cls = cls;
        this.name = name;
        this.icon = icon;
    }

    @Override
    public String toString() {
        return cls.getSimpleName();
    }


    public Class<IHandler<? extends HttpRequest>> getHandlerClass() {
        return cls;
    }


    public String getName() {
        return name;
    }


    public File getIcon() {
        return icon;
    }



}
