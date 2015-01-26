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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import net.objectof.actof.common.controller.IActofUIController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.config.Env;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.minion.Settings;
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

    private static final String SETTING_PATH = "net.objectof.actof.minion.spring.path";

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

            // build our classloader
            IsolatedClassLoader loader = new IsolatedClassLoader();
            for (File file : classpathFiles) {
                loader.addURL(file.toURI().toURL());
            }

            // Create webapp directory/file structure
            Path rootDir = Files.createTempDirectory("Minion Server");
            rootDir.toFile().deleteOnExit();
            File webinf = new File(rootDir.toFile(), "WEB-INF");
            File appxml = new File(webinf, "app.xml");
            File webxml = new File(webinf, "web.xml");
            webinf.mkdirs();

            // write web.xml
            Scanner scanner = new Scanner(SpringController.class.getResourceAsStream("web.xml"));
            String webxmlContent = scanner.useDelimiter("\\Z").next();
            scanner.close();
            FileWriter writer = new FileWriter(webxml);
            writer.write(webxmlContent);
            writer.close();

            // write app.xml - bean config
            writer = new FileWriter(appxml);
            writer.write(beans.getText());
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

    public void open() throws FileNotFoundException {

        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(Settings.get(SETTING_PATH, Env.homeDirectory()));
        File config = chooser.showOpenDialog(null);
        if (config == null) { return; }
        Settings.put(SETTING_PATH, config.getParentFile());

        Scanner scanner = new Scanner(config);
        scanner.useDelimiter("\\Z");
        beans.setText(scanner.next());
        scanner.close();
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

    public static SpringController load(ChangeController changes) throws IOException {
        return FXUtil.load(SpringController.class, "Spring.fxml", changes);
    }

}
