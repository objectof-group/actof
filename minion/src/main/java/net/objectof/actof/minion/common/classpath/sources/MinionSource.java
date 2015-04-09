package net.objectof.actof.minion.common.classpath.sources;


import java.net.URL;
import java.util.Collection;

import net.objectof.actof.minion.common.classpath.IsolatedClassLoader;
import net.objectof.actof.minion.common.minionhandler.MinionHandler;


public interface MinionSource {

    String getTitle();

    String getPath();

    Collection<URL> getURLs();

    Collection<MinionHandler> getHandlers();

    void refresh(IsolatedClassLoader loader);

    boolean isDeployable();
}
