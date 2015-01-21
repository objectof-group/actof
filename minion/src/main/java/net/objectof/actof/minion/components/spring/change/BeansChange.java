package net.objectof.actof.minion.components.spring.change;


import net.objectof.actof.common.controller.change.Change;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;


public class BeansChange extends Change {

    private Object root;
    private BeanDefinitionRegistry registry;

    public BeansChange(Object root, BeanDefinitionRegistry registry) {
        super();
        this.root = root;
        this.registry = registry;
    }

    public Object getRoot() {
        return root;
    }

    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

}
