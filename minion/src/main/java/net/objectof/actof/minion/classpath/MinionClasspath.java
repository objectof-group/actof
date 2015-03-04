package net.objectof.actof.minion.classpath;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.objectof.actof.minion.classpath.sources.MinionSource;


public class MinionClasspath implements Iterable<MinionSource> {

    private List<MinionSource> sources;
    private IsolatedClassLoader loader;

    public MinionClasspath() {
        sources = new ArrayList<>();
    }

    public MinionSource addSource(MinionSource source) {
        source.getHandlers().clear();
        sources.add(source);
        refresh();
        return source;
    }

    public MinionSource removeSource(MinionSource source) {
        sources.remove(source);
        refresh();
        return source;
    }

    public Iterator<MinionSource> iterator() {
        return sources.iterator();
    }

    private void refresh() {
        loader = new IsolatedClassLoader();

        for (MinionSource source : sources) {
            loader.addURLs(source.getURLs());
        }
        for (MinionSource source : sources) {
            source.refresh(loader);
        }
    }

}
