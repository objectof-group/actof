package net.objectof.actof.repospy.controllers.navigator;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.objectof.actof.common.controller.change.Change;
import net.objectof.model.Resource;


public class ResourceSelectedChange extends Change {

    private List<Resource<?>> resources;
    private boolean append;

    public ResourceSelectedChange(Resource<?> res, boolean append) {
        this.resources = Collections.singletonList(res);
        this.append = append;
    }

    public ResourceSelectedChange(List<Resource<?>> res) {
        resources = new ArrayList<>(res);
        this.append = false;

    }

    public ResourceSelectedChange(Resource<?>... res) {
        resources = new ArrayList<>(Arrays.asList(res));
        this.append = false;
    }

    public List<Resource<?>> getResources() {
        return resources;
    }

    protected boolean isAppend() {
        return append;
    }

}
