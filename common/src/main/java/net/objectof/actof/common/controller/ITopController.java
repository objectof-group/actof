package net.objectof.actof.common.controller;


import net.objectof.actof.common.controller.change.IChangeController;


public class ITopController extends IActofController {

    public ITopController() {
        super(new IChangeController());
    }

}
