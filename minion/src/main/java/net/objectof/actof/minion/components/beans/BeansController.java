package net.objectof.actof.minion.components.beans;


import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import net.objectof.actof.common.controller.IActofUIController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.minion.components.spring.change.BeansChange;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;


public class BeansController extends IActofUIController {

    @FXML
    private TreeTableView<String> beans;
    @FXML
    private TreeTableColumn<String, String> names;

    private BeanDefinitionRegistry registry;

    @Override
    public void ready() {
        // TODO Auto-generated method stub

        beans.setRoot(new TreeItem<>(null));
        names.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue()));

        getChangeBus().listen(BeansChange.class, change -> {
            registry = change.getRegistry();
            TreeItem<String> root = new TreeItem<>(null);
            beans.setRoot(root);

            for (String name : registry.getBeanDefinitionNames()) {
                System.out.println(name);
                root.getChildren().add(new TreeItem(name));
            }
        });

        beans.getSelectionModel().selectedItemProperty().addListener(change -> {
            showBean(beans.getSelectionModel().getSelectedItem().getValue());
        });
    }

    private void showBean(String name) {

        if (name == null) {
            // clear view
        }

        BeanDefinition def = registry.getBeanDefinition(name);
        ConstructorArgumentValues args = def.getConstructorArgumentValues();
        for (ValueHolder val : args.getGenericArgumentValues()) {
            System.out.println(val.toString());
            System.out.println("    " + val.getName());
            System.out.println("    " + val.getValue());
        }
    }

    @Override
    protected void initialize() throws Exception {
        // TODO Auto-generated method stub

    }

    public static BeansController load(ChangeController changes) throws IOException {
        return FXUtil.load(BeansController.class, "Beans.fxml", changes);
    }

}
