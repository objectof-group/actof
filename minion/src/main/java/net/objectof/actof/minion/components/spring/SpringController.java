package net.objectof.actof.minion.components.spring;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import net.objectof.actof.common.controller.IActofUIController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.minion.components.classpath.change.ClasspathChange;
import net.objectof.actof.minion.components.spring.change.BeansChange;
import net.objectof.actof.widgets.StatusLight;
import net.objectof.actof.widgets.StatusLight.Status;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;


public class SpringController extends IActofUIController {

    @FXML
    private TextArea beans;
    @FXML
    private TextField rootBean;
    @FXML
    private VBox topBox;

    private StatusLight status;

    private List<File> classpathFiles = Collections.emptyList();

    @Override
    public void ready() {

        status = new StatusLight();
        status.setStatus(Status.OFF, "No Server Configuration Defined");
        topBox.getChildren().add(status);

        beans.setStyle("-fx-font-family: Monospaced;");
        beans.setText("<beans xmlns='http://www.springframework.org/schema/beans' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation='http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd'>\n\n\n</beans>");

        rootBean.setText("root");

        getChangeBus().listen(ClasspathChange.class, event -> {
            classpathFiles = event.getFiles();
        });
    }

    @Override
    protected void initialize() throws Exception {}

    public void apply() {

        try {

            List<URL> urls = new ArrayList<>();
            for (File file : classpathFiles) {
                urls.add(file.toURI().toURL());
            }

            DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
            XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(registry);

            ClassLoader loader = new URLClassLoader(urls.toArray(new URL[] {}), Thread.currentThread()
                    .getContextClassLoader());

            Resource res = new ByteArrayResource(beans.getText().getBytes());
            reader.setBeanClassLoader(loader);
            reader.loadBeanDefinitions(res);

            Object root = registry.getBean(rootBean.getText());
            getChangeBus().broadcast(new BeansChange(root, registry));

            status.setStatus(Status.GOOD, "Server Configuration Built at " + new Date());
        }
        catch (Exception e) {
            status.setStatus(Status.BAD, e);
        }

    }

    public static SpringController load(ChangeController changes) throws IOException {
        return FXUtil.load(SpringController.class, "Spring.fxml", changes);
    }

}
