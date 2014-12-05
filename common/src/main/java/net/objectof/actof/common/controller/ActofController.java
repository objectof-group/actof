package net.objectof.actof.common.controller;


import net.objectof.actof.common.controller.change.ChangeController;



public interface ActofController {

    ChangeController getChangeBus();

}