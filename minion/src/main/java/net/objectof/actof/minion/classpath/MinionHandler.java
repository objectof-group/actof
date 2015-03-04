package net.objectof.actof.minion.classpath;


import java.io.File;


public class MinionHandler {

    String name, simplename;
    File icon;

    public MinionHandler(Class<?> cls) {
        name = cls.getName();
        simplename = cls.getSimpleName();
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return simplename;
    }

    public File getIcon() {
        return icon;
    }

    public void setIcon(File icon) {
        this.icon = icon;
    }

    public String toString() {
        return getTitle();
    }

}
