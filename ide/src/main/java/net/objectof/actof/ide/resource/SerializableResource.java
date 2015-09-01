package net.objectof.actof.ide.resource;


import java.util.Map;


public class SerializableResource {

    public String title;
    public String cls;
    public Map<String, Object> map;

    public SerializableResource() {}

    public SerializableResource(String title, String cls, Map<String, Object> map) {
        this.title = title;
        this.cls = cls;
        this.map = map;
    }

}
