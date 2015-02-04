package net.objectof.actof.minion.components.spring;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import net.objectof.actof.common.util.ActofUtil;


public class BeanDefinition {

    private SimpleStringProperty name = new SimpleStringProperty("Untitled");
    private File file;
    private String contents;

    public BeanDefinition() {
        file = null;
        contents = "";
    }

    public BeanDefinition(File input) throws FileNotFoundException {
        this(new FileInputStream(input), input);
    }

    public BeanDefinition(InputStream input, File file) {
        setFile(file);
        contents = ActofUtil.readFile(input);
    }

    public BeanDefinition(String input) {
        contents = input;
        file = null;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
        name.set(file.getName());
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String toString() {
        return name.get();
    }

    public Observable[] observables() {
        return new Observable[] { name };
    }
}
