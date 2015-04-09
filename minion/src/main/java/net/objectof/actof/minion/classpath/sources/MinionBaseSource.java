package net.objectof.actof.minion.classpath.sources;


import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.objectof.actof.minion.classpath.IsolatedClassLoader;
import net.objectof.actof.minion.classpath.MinionHandler;
import net.objectof.actof.minion.components.handlers.style.HandlerCategory;

import org.reflections.Reflections;


public class MinionBaseSource implements MinionSource {

    private List<MinionHandler> handlers = new ArrayList<>();

    @Override
    public String getTitle() {
        return "Base Packages";
    }

    @Override
    public String getPath() {
        return "";
    }

    @Override
    public Collection<URL> getURLs() {
        URL[] urls = ((URLClassLoader) (Thread.currentThread().getContextClassLoader())).getURLs();
        return Arrays.asList(urls);
    }

    public static Collection<URL> getBaseURLs() {
        URL[] urls = ((URLClassLoader) (Thread.currentThread().getContextClassLoader())).getURLs();
        return Arrays.asList(urls);
    }

    @Override
    public Collection<MinionHandler> getHandlers() {
        return handlers;
    }

    @Override
    public void refresh(IsolatedClassLoader loader) {
        handlers.clear();
        Reflections ref = new Reflections(loader);

        try {

            Class<?> handlerClass = loader.loadClass("net.objectof.corc.Handler");
            for (Class cls : ref.getSubTypesOf(handlerClass)) {

                if (!cls.getName().startsWith("net.objectof")) continue;
                if (cls.isSynthetic()) continue;
                if (cls.isInterface()) continue;
                if (Modifier.isAbstract(cls.getModifiers())) continue;
                if (cls.isAnonymousClass()) continue;

                MinionHandler handler = new MinionHandler(cls, HandlerCategory.GENERIC);
                handlers.add(handler);

            }
        }
        catch (ClassNotFoundException e) {

        }
    }

    @Override
    public boolean isDeployable() {
        return false;
    }
}
