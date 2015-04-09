package net.objectof.actof.minion.components.classpath;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.FileChooser;
import net.objectof.actof.common.controller.IActofUIController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.config.Env;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.minion.Settings;
import net.objectof.actof.minion.classpath.MinionClasspath;
import net.objectof.actof.minion.classpath.minionhandler.MinionHandler;
import net.objectof.actof.minion.classpath.sources.MinionJarSource;
import net.objectof.actof.minion.classpath.sources.MinionSource;
import net.objectof.actof.minion.components.classpath.change.ClasspathChange;
import net.objectof.actof.minion.util.Worker;


public class ClasspathController extends IActofUIController {

    private static final String SETTING_PATH = "net.objectof.actof.minion.spring.path";

    @FXML
    public TreeTableView<Object> jarTable;

    @FXML
    public TreeTableColumn<Object, String> handlerColumn, classnameColumn;

    private MinionClasspath classpath = new MinionClasspath();

    @Override
    public void ready() {

        jarTable.setShowRoot(false);
        jarTable.setRoot(new TreeItem<Object>());
        buildTable();

        getChangeBus().listen(ClasspathChange.class, () -> buildTable());

        handlerColumn.setCellValueFactory(item -> {
            Object val = item.getValue().getValue();
            String result = "";
            if (val instanceof MinionSource) {
                result = ((MinionSource) val).getTitle();
            } else if (val instanceof MinionHandler) {
                result = ((MinionHandler) val).getTitle();
            }
            return new SimpleStringProperty(result);
        });

        classnameColumn.setCellValueFactory(item -> {
            Object val = item.getValue().getValue();
            String result = "";
            if (val instanceof MinionSource) {
                result = ((MinionSource) val).getPath();
            } else if (val instanceof MinionHandler) {
                result = ((MinionHandler) val).getClass().getName();
            }
            return new SimpleStringProperty(result);
        });

        // Worker.first(() -> classpath.addSource(new
        // MinionBaseSource())).then(this::addJar).run();

    }

    @Override
    protected void initialize() throws Exception {}

    public void addJar() throws MalformedURLException, IOException {

        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(Settings.get(SETTING_PATH, Env.homeDirectory()));
        List<File> jars = chooser.showOpenMultipleDialog(null);
        if (jars == null || jars.size() == 0) { return; }
        Settings.put(SETTING_PATH, jars.get(0).getParentFile());

        for (File jar : jars) {
            Worker.first(() -> {
                MinionJarSource handlerJar = new MinionJarSource(jar);
                classpath.addSource(handlerJar);
                return handlerJar;
            }).then(this::addJar).run();
        }

    }

    private void addJar(MinionSource jar) {
        getChangeBus().broadcast(new ClasspathChange(classpath));
    }

    public void removeJar() {
        TreeItem<Object> selected = jarTable.getSelectionModel().getSelectedItem();
        MinionSource source = (MinionSource) selected.getValue();
        classpath.removeSource(source);

        if (!jarTable.getRoot().getChildren().contains(selected)) { return; }

        jarTable.getRoot().getChildren().remove(selected);

        getChangeBus().broadcast(new ClasspathChange(classpath));
    }

    private void buildTable() {

        jarTable.getRoot().getChildren().clear();

        for (MinionSource jar : classpath) {

            TreeItem<Object> jarnode = new TreeItem<Object>(jar);
            jarTable.getRoot().getChildren().add(jarnode);

            for (MinionHandler handler : jar.getHandlers()) {
                TreeItem<Object> handlernode = new TreeItem<Object>(handler);
                jarnode.getChildren().add(handlernode);
            }
        }
    }

    public static ClasspathController load(ChangeController changes) throws IOException {
        return FXUtil.load(ClasspathController.class, "Classpath.fxml", changes);
    }

}
