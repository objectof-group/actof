package net.objectof.actof.minion.components.spring;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import net.objectof.actof.common.controller.IActofUIController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.config.Env;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.minion.Settings;
import net.objectof.actof.minion.components.classpath.ClasspathController;
import net.objectof.actof.minion.components.classpath.change.ClasspathChange;
import net.objectof.actof.minion.components.spring.change.HandlerChange;
import net.objectof.actof.widgets.StatusLight;
import net.objectof.actof.widgets.StatusLight.Status;

import org.eclipse.jetty.webapp.WebAppContext;


public class SpringController extends IActofUIController {

    @FXML
    private TextArea beans;
    @FXML
    private TextField rootBean;
    @FXML
    private VBox topBox;
    @FXML
    private ListView<BeanDefinition> beanList;
    @FXML
    private TitledPane classpathBox, beansBox;
    @FXML
    private Accordion accordion;

    private static final String SETTING_PATH = "net.objectof.actof.minion.spring.path";

    private StatusLight status;

    private List<File> classpathFiles = Collections.emptyList();

    @Override
    public void ready() {

        status = new StatusLight();
        status.setStatus(Status.OFF, "No Server Configuration Defined");
        topBox.getChildren().add(status);

        beans.setStyle("-fx-font-family: Monospaced;");

        rootBean.setText("root");

        getChangeBus().listen(ClasspathChange.class, event -> {
            classpathFiles = event.getFiles();
        });

        beanList.getSelectionModel().selectedItemProperty().addListener(change -> {
            BeanDefinition def = beanList.getSelectionModel().getSelectedItem();
            beans.setText(def.getContents());
        });

        beans.textProperty().addListener(change -> {
            beanList.getSelectionModel().getSelectedItem().setContents(beans.getText());
        });

        BeanDefinition app = new BeanDefinition(
                "<beans xmlns='http://www.springframework.org/schema/beans' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation='http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd'>\n\n\n</beans>",
                "app.xml");
        beanList.getItems().add(app);
        beanList.getSelectionModel().select(app);

        try {
            classpathBox.setContent(ClasspathController.load(getChangeBus()).getNode());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        accordion.setExpandedPane(beansBox);

    }

    @Override
    protected void initialize() throws Exception {}

    public void apply() {

        try {

            FileWriter writer;

            // build our classloader
            IsolatedClassLoader loader = new IsolatedClassLoader();
            for (File file : classpathFiles) {
                loader.addURL(file.toURI().toURL());
            }

            // Create webapp directory/file structure
            Path rootDir = Files.createTempDirectory("Minion Server");
            rootDir.toFile().deleteOnExit();
            File webinf = new File(rootDir.toFile(), "WEB-INF");
            File webxml = new File(webinf, "web.xml");
            webinf.mkdirs();

            // write user xml files
            for (BeanDefinition def : beanList.getItems()) {
                File file = new File(webinf, def.getFilename());
                writer = new FileWriter(file);
                writer.write(def.getContents());
                writer.close();
            }

            // generate web.xml contents
            Scanner scanner = new Scanner(SpringController.class.getResourceAsStream("web.xml"));
            String beansdef = scanner.useDelimiter("\\Z").next();
            scanner.close();
            beansdef = beansdef.replace("[[[rootbean]]]", rootBean.getText());
            String configFiles = beanList.getItems().stream().map(bean -> "WEB-INF/" + bean.getFilename())
                    .reduce((acc, file) -> acc + "," + file).get();
            beansdef = beansdef.replace("[[[configfiles]]]", configFiles);

            // write web.xml
            writer = new FileWriter(webxml);
            writer.write(beansdef);
            writer.close();

            // Create the WebAppContext handler
            WebAppContext context = new WebAppContext();
            context.setClassLoader(loader);
            context.setContextPath("");
            context.setResourceBase(rootDir.toString());

            // Transmit new WebAppContext as event
            getChangeBus().broadcast(new HandlerChange(context));
            status.setStatus(Status.GOOD, "Server Configuration Built at " + new Date());

        }
        catch (Exception e) {
            status.setStatus(Status.BAD, e);
        }

    }

    public void add() throws FileNotFoundException {

        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(Settings.get(SETTING_PATH, Env.homeDirectory()));
        List<File> files = chooser.showOpenMultipleDialog(null);
        if (files == null) { return; }
        if (files.size() == 0) { return; }
        Settings.put(SETTING_PATH, files.get(0).getParentFile());

        for (File file : files) {
            addBeanDef(new BeanDefinition(file, file.getName()));
        }

    }

    public void save() throws IOException {

        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(Settings.get(SETTING_PATH, Env.homeDirectory()));
        File config = chooser.showSaveDialog(null);
        if (config == null) { return; }
        Settings.put(SETTING_PATH, config.getParentFile());

        if (config.exists()) {
            Alert alert = new Alert(AlertType.CONFIRMATION, "Overwrite existing file?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> response = alert.showAndWait();
            if (!response.isPresent()) { return; }
            if (response.get() != ButtonType.YES) { return; }
        }

        Writer writer = new OutputStreamWriter(new FileOutputStream(config));
        writer.write(beans.getText());
        writer.close();

    }

    public void remove() {
        BeanDefinition def = beanList.getSelectionModel().getSelectedItem();
        int index = beanList.getSelectionModel().getSelectedIndex();
        if (index == 0) { return; }
        if (def == null) { return; }
        beanList.getItems().remove(def);
        beanList.getSelectionModel().select(0);
    }

    private void addBeanDef(BeanDefinition def) {
        beanList.getItems().add(def);
    }

    public static SpringController load(ChangeController changes) throws IOException {
        return FXUtil.load(SpringController.class, "Spring.fxml", changes);
    }

}
