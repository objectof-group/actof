package net.objectof.actof.minion.components.server.change;


import net.objectof.actof.common.controller.change.Change;


public class ServerStartChange extends Change {

    private String url;

    public ServerStartChange(String url) {
        this.url = url;
    }


    public String getUrl() {
        return url;
    }



}
