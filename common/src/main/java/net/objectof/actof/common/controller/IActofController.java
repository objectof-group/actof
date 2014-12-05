package net.objectof.actof.common.controller;


import net.objectof.actof.common.controller.change.ChangeController;



public class IActofController implements ActofController {

    private ChangeController changes;


    public IActofController(ChangeController changes) {
        this.changes = changes;
    }

    @Override
    public final ChangeController getChangeBus() {
        return changes;
    }


}