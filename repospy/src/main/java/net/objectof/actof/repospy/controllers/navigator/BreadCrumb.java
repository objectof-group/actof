package net.objectof.actof.repospy.controllers.navigator;


import net.objectof.model.Resource;


public class BreadCrumb {

    private Resource<?> res;

    public BreadCrumb(Resource<?> res) {
        this.res = res;
    }

    protected Resource<?> getRes() {
        return res;
    }

    public String toString() {
        return res.id().kind().getComponentName() + "-" + res.id().label().toString();
    }

}
