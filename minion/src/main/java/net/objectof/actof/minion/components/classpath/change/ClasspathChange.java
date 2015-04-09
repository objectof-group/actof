package net.objectof.actof.minion.components.classpath.change;


import net.objectof.actof.common.controller.change.Change;
import net.objectof.actof.minion.common.classpath.sources.MinionClasspath;


public class ClasspathChange extends Change {

    private MinionClasspath classpath;

    public ClasspathChange(MinionClasspath classpath) {
        this.classpath = classpath;
    }

    public MinionClasspath getClasspath() {
        return classpath;
    }

}
