package net.objectof.actof.minion.components.spring;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import net.objectof.actof.common.controller.IActofUIController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.minion.components.spring.change.BeansChange;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;


public class SpringController extends IActofUIController {

    @FXML
    private TextArea beans;
    @FXML
    private TextField rootBean;

    @FXML
    private ListView<String> jarList;

    private ApplicationContext ctx;

    @Override
    public void ready() {
        beans.setStyle("-fx-font-family: Monospaced;");
        beans.setText("<beans xmlns='http://www.springframework.org/schema/beans' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation='http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd'>\n\n\n</beans>");

        rootBean.setText("root");
    }

    @Override
    protected void initialize() throws Exception {
        // TODO Auto-generated method stub

    }

    public void loadBeans() {
        Resource res = new ByteArrayResource(beans.getText().getBytes());
        ctx = new GenericXmlApplicationContext(res);
    }

    public Object getRootBean() {
        if (ctx == null) { return null; }
        return ctx.getBean(rootBean.getText());
    }

    public void apply() {
        loadBeans();
        Object root = getRootBean();
        getChangeBus().broadcast(new BeansChange(root));
    }

    public void loadJar() throws MalformedURLException, IOException {
        FileChooser chooser = new FileChooser();
        File jar = chooser.showOpenDialog(null);
        loadJar(jar.toURL());
        jarList.getItems().add(jar.getName());
    }

    public static void loadJar(URL u) throws IOException {

        Class[] parameters = new Class[] { URL.class };

        URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class sysclass = URLClassLoader.class;

        try {
            Method method = sysclass.getDeclaredMethod("addURL", parameters);
            method.setAccessible(true);
            method.invoke(sysloader, new Object[] { u });
        }
        catch (Throwable t) {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        }// end try catch

    }// end method

    public static SpringController load(ChangeController changes) throws IOException {
        return FXUtil.load(SpringController.class, "Spring.fxml", changes);
    }

}
