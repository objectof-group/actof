package net.objectof.actof.minion.components.handlers;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import net.objectof.impl.corc.IHandler;


public class HandlerLoader {

    List<File> jars = new ArrayList<>();
    Set<HandlerClass> handlers = new HashSet<>();


    public boolean add(File e) {
        return jars.add(e);
    }

    public boolean remove(File f) {
        return jars.remove(f);
    }

    public void clear() {
        jars.clear();
    }

    public List<HandlerClass> getHandlers() {
        return new ArrayList<>(handlers);
    }


    public boolean isWebHandler(Class<IHandler<?>> handler) {

        try {

            if (!IHandler.class.isAssignableFrom(handler)) { return false; }

            /*
             * IHandler<?> instance = handler.newInstance(); Class datatype =
             * instance.getArgumentClass();
             * 
             * if (!HttpRequest.class.isAssignableFrom(datatype)) { return
             * false; }
             */

            return true;
        }
        catch (SecurityException | IllegalArgumentException e) {
            return false;
        }


    }




    public static List<ZipEntry> getEntries(File jar) throws ZipException, IOException {
        List<ZipEntry> list = new ArrayList<>();
        ZipFile zipFile = new ZipFile(jar);

        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            list.add(entry);
        }

        zipFile.close();

        return list;
    }

    // messy logic for getting a list of all available handlers from inside of a
    // jar file
    public void loadJar(File jarFile) throws ZipException, IOException {

        // File jarFile: the jar file we want to look inside of
        List<ZipEntry> entries = getEntries(jarFile);

        // get this jar file loaded by a class loader
        URL jarurl = new URL("jar", "", "file:" + jarFile.getAbsolutePath() + "!/");
        URLClassLoader cl = URLClassLoader.newInstance(new URL[] { jarurl });

        for (ZipEntry entry : entries) {

            if (entry.isDirectory()) continue;
            if (!entry.getName().endsWith(".class")) continue;
            String classPathName = entry.getName().substring(0, entry.getName().length() - 6);
            String qualifiedClassName = classPathName.replace("/", ".");

            Class cls;
            try {
                cls = cl.loadClass(qualifiedClassName);
                if (isWebHandler(cls)) {
                    handlers.add(new HandlerClass(cls, cls.getSimpleName(), null));
                }
            }
            catch (Throwable exception) {
                continue;
            }

        }


    }


}
