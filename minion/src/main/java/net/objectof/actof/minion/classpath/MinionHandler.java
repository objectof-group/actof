package net.objectof.actof.minion.classpath;


import java.io.File;

import net.objectof.actof.minion.components.handlers.style.HandlerCategory;


public class MinionHandler {

    String name, simplename;
    File icon;
    HandlerCategory category;

    public MinionHandler(Class<?> cls) {
        name = cls.getName();
        simplename = cls.getSimpleName();
    }

    public MinionHandler(MinionHandler other) {
        this.name = other.name;
        this.simplename = other.simplename;
        this.icon = other.icon;
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

    public HandlerCategory getCategory() {
        return category;
    }

    public void setCategory(HandlerCategory category) {
        this.category = category;
    }

}
