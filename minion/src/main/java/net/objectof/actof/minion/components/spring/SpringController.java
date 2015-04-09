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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import javafx.collections.FXCollections;
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
import net.objectof.actof.common.util.ActofUtil;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.minion.Settings;
import net.objectof.actof.minion.common.classpath.IsolatedClassLoader;
import net.objectof.actof.minion.common.classpath.sources.MinionClasspath;
import net.objectof.actof.minion.common.classpath.sources.MinionSource;
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
    private TitledPane beansBox;
    @FXML
    private Accordion accordion;

    private static final String SETTING_PATH = "net.objectof.actof.minion.spring.path";

    private StatusLight status;

    private MinionClasspath classpath;

    @Override
    public void ready() {

        status = new StatusLight();
        status.setStatus(Status.OFF, "No Server Configuration Defined");
        topBox.getChildren().add(status);

        beans.setStyle("-fx-font-family: Monospaced;");
        beans.textProperty().addListener(change -> {
            BeanDefinition def = beanList.getSelectionModel().getSelectedItem();
            if (def == null) { return; }
            def.setContents(beans.getText());
        });

        rootBean.setText("root");

        beanList.getSelectionModel().selectedItemProperty().addListener(change -> {
            BeanDefinition def = beanList.getSelectionModel().getSelectedItem();
            if (def == null) {
                beans.setText("");
                beans.setDisable(true);
            } else {
                beans.setText(def.getContents());
                beans.setDisable(false);
            }
        });

        beanList.setItems(FXCollections.observableArrayList(person -> person.observables()));

        accordion.setExpandedPane(beansBox);

        getChangeBus().listen(ClasspathChange.class, event -> classpath = event.getClasspath());
    }

    @Override
    protected void initialize() throws Exception {}

    public void buildWebApp() {

        try {

            FileWriter writer;

            // build our classloader
            IsolatedClassLoader loader = new IsolatedClassLoader();
            for (MinionSource source : classpath) {
                if (!source.isDeployable()) continue;
                loader.addURLs(source.getURLs());
            }

            // Create webapp directory/file structure
            Path rootDir = Files.createTempDirectory("Minion Server");
            rootDir.toFile().deleteOnExit();
            File webinf = new File(rootDir.toFile(), "WEB-INF");
            File webxml = new File(webinf, "web.xml");
            webinf.mkdirs();

            // write user xml files
            for (BeanDefinition def : beanList.getItems()) {
                File file = new File(webinf, def.getFile().getName());
                writer = new FileWriter(file);
                writer.write(def.getContents());
                writer.close();
                file.deleteOnExit();
            }

            // generate web.xml contents
            Scanner scanner = new Scanner(SpringController.class.getResourceAsStream("web.xml"));
            String beansdef = scanner.useDelimiter("\\Z").next();
            scanner.close();
            beansdef = beansdef.replace("[[[rootbean]]]", rootBean.getText());
            String configFiles = beanList.getItems().stream().map(bean -> "WEB-INF/" + bean.getFile().getName())
                    .reduce((acc, file) -> acc + "," + file).get();
            beansdef = beansdef.replace("[[[configfiles]]]", configFiles);

            // write web.xml
            writer = new FileWriter(webxml);
            writer.write(beansdef);
            writer.close();
            webxml.deleteOnExit();

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

    public void newBeanDefinition() throws FileNotFoundException {
        BeanDefinition app = new BeanDefinition(
                "<beans xmlns='http://www.springframework.org/schema/beans' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation='http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd'>\n\n\n</beans>");
        beanList.getItems().add(app);
        beanList.getSelectionModel().select(app);
    }

    public void addBeanDefinition() throws FileNotFoundException {

        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(Settings.get(SETTING_PATH, Env.homeDirectory()));
        List<File> files = chooser.showOpenMultipleDialog(null);
        if (files == null) { return; }
        if (files.size() == 0) { return; }
        Settings.put(SETTING_PATH, files.get(0).getParentFile());

        for (File file : files) {
            addBeanDefinition(file);
        }

    }

    public void addBeanDefinition(File file) throws FileNotFoundException {
        beanList.getItems().add(new BeanDefinition(file));
    }

    public void saveBeanDefinition() throws IOException {

        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(Settings.get(SETTING_PATH, Env.homeDirectory()));
        File beansFile = chooser.showSaveDialog(null);
        if (beansFile == null) { return; }
        Settings.put(SETTING_PATH, beansFile.getParentFile());

        if (beansFile.exists()) {
            Alert alert = new Alert(AlertType.CONFIRMATION, "Overwrite existing file?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> response = alert.showAndWait();
            if (!response.isPresent()) { return; }
            if (response.get() != ButtonType.YES) { return; }
        }

        Writer writer = new OutputStreamWriter(new FileOutputStream(beansFile));
        writer.write(beans.getText());
        writer.close();

        BeanDefinition def = beanList.getSelectionModel().getSelectedItem();
        def.setFile(beansFile);
        beanList.getSelectionModel().select(def);

    }

    public void removeBeanDefinition() {
        BeanDefinition def = beanList.getSelectionModel().getSelectedItem();
        if (def == null) { return; }
        beanList.getItems().remove(def);
        beanList.getSelectionModel().select(0);
    }

    public void openProject() throws FileNotFoundException {
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(Settings.get(SETTING_PATH, Env.homeDirectory()));
        File file = chooser.showOpenDialog(null);
        if (file == null) { return; }

        String json = ActofUtil.readFile(file);
        MinionProject project = ActofUtil.deserialize(json, MinionProject.class);
        beanList.getItems().setAll(project.beanDefs);
        // TODO: Fix this
        // jarList.getItems().setAll(project.jars.stream().map(s -> new
        // File(s)).collect(Collectors.toList()));
        rootBean.setText(project.rootbean);

    }

    public void saveProject() throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(Settings.get(SETTING_PATH, Env.homeDirectory()));
        File file = chooser.showSaveDialog(null);
        if (file == null) { return; }

        // build a project object and serialize it
        MinionProject project = new MinionProject();
        project.beanDefs = beanList.getItems();
        // TODO: Fix this
        // project.jars = jarList.getItems().stream().map(f ->
        // f.toString()).collect(Collectors.toList());
        project.rootbean = rootBean.getText();
        String json = ActofUtil.serialize(project);
        FileWriter writer = new FileWriter(file);
        writer.write(json);
        writer.close();

    }

    public static SpringController load(ChangeController changes) throws IOException {
        return FXUtil.load(SpringController.class, "Spring.fxml", changes);
    }

}
