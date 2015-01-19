package net.objectof.actof.minion.components.classpath.change;


import java.io.File;
import java.util.List;

import net.objectof.actof.common.controller.change.Change;


public class ClasspathChange extends Change {

    List<File> files;

    public ClasspathChange(List<File> files) {
        this.files = files;
    }

    public List<File> getFiles() {
        return files;
    }

}
