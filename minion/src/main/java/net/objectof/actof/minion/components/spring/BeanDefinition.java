package net.objectof.actof.minion.components.spring;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import net.objectof.actof.common.util.ActofUtil;


public class BeanDefinition {

    private String filename;
    private String contents;

    public BeanDefinition(File input, String filename) throws FileNotFoundException {
        this(new FileInputStream(input), filename);
    }

    public BeanDefinition(InputStream input, String filename) {
        this.filename = filename;
        contents = ActofUtil.readFile(input);
    }

    public BeanDefinition(String input, String filename) {
        this.contents = input;
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

}
