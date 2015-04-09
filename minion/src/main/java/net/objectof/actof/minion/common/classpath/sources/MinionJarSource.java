package net.objectof.actof.minion.common.classpath.sources;


import java.io.File;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.objectof.actof.minion.common.classpath.IsolatedClassLoader;
import net.objectof.actof.minion.common.minionhandler.MinionHandler;
import net.objectof.actof.minion.common.minionhandler.MinionHandler.Category;

import org.reflections.Reflections;
import org.reflections.ReflectionsException;
import org.reflections.util.ClasspathHelper;


public class MinionJarSource implements MinionSource {

    private File jarfile;
    private URL jarurl;
    private List<MinionHandler> handlers = new ArrayList<>();

    public MinionJarSource(File jarfile) throws MalformedURLException {

        this.jarfile = jarfile;
        this.jarurl = jarfile.toURI().toURL();

    }

    public String getTitle() {
        return jarfile.getName();
    }

    public String getPath() {
        return jarfile.getAbsolutePath();
    }

    @Override
    public Collection<URL> getURLs() {
        return Collections.singleton(jarurl);
    }

    public List<MinionHandler> getHandlers() {
        return handlers;
    }

    public void refresh(IsolatedClassLoader loader) {

        try {

            handlers.clear();
            Reflections ref = new Reflections(loader);

            Class<?> handlerClass = loader.loadClass("net.objectof.corc.Handler");
            for (Class cls : ref.getSubTypesOf(handlerClass)) {

                URL url = ClasspathHelper.forClass(cls, loader);
                if (!url.getProtocol().equals("jar")) continue;
                System.out.println(url);
                url = new URL(url.getPath());
                String jarpath = url.getPath();
                jarpath = jarpath.substring(0, jarpath.length() - 2);
                System.out.println(jarpath);
                System.out.println("------------------------------");

                if (!jarpath.equals(jarfile.getAbsolutePath())) continue;

                if (cls.isSynthetic()) continue;
                if (cls.isInterface()) continue;
                if (Modifier.isAbstract(cls.getModifiers())) continue;
                if (cls.isAnonymousClass()) continue;

                Category cat;
                cat = Category.values()[(int) (Math.random() * Category.values().length)];
                MinionHandler handler = new MinionHandler(cls, cat);
                handlers.add(handler);
            }

        }
        catch (ReflectionsException | ClassNotFoundException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isDeployable() {
        return true;
    }

}
