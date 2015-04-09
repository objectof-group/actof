package net.objectof.actof.minion.common.classpath;


import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;


public class IsolatedClassLoader extends URLClassLoader {

    public IsolatedClassLoader() {
        super(new URL[] {});
    }

    public void addURL(URL url) {
        super.addURL(url);
    }

    public void addURLs(Collection<URL> urls) {
        for (URL url : urls) {
            addURL(url);
        }
    }

    public void addURLs(URL[] urls) {
        for (URL url : urls) {
            addURL(url);
        }
    }

    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {

            if (!isLocal(name)) { return super.loadClass(name, resolve); }

            Class<?> c = findLoadedClass(name);

            if (c == null) {
                c = findClass(name);
            }

            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }

    private boolean isLocal(String name) {

        if (name.startsWith("net.objectof")) { return true; }
        return false;
        // return true;

    }

}
