package net.objectof.actof.minion.components.spring.change;


import net.objectof.actof.common.controller.change.Change;


public class BeansChange extends Change {

    private Object root;

    public BeansChange(Object root) {
        super();
        this.root = root;
    }

    public Object getRoot() {
        return root;
    }

}
