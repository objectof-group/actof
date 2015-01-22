package net.objectof.actof.minion.components.classpath;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

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
import net.objectof.actof.minion.components.classpath.change.ClasspathChange;


public class ClasspathController extends IActofUIController {

    @FXML
    private TreeTableView<File> jarList;
    @FXML
    private TreeTableColumn<File, String> jarFile, jarPath;

    private static final String SETTING_PATH = "net.objectof.actof.minion.classpath.path";

    @Override
    public void ready() {
        // TODO Auto-generated method stub
        jarList.setRoot(new TreeItem<>());

        jarFile.setCellValueFactory(features -> {
            return new SimpleStringProperty(features.getValue().getValue().getName());
        });
        jarFile.setPrefWidth(200);

        jarPath.setCellValueFactory(features -> {
            return new SimpleStringProperty(features.getValue().getValue().getAbsolutePath());
        });
        jarPath.setPrefWidth(500);
    }

    @Override
    protected void initialize() throws Exception {
        // TODO Auto-generated method stub

    }

    public static ClasspathController load(ChangeController changes) throws IOException {
        return FXUtil.load(ClasspathController.class, "Classpath.fxml", changes);
    }

    public void addJar() throws MalformedURLException, IOException {

        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(Settings.get(SETTING_PATH, Env.homeDirectory()));
        List<File> jars = chooser.showOpenMultipleDialog(null);
        if (jars == null || jars.size() == 0) { return; }
        Settings.put(SETTING_PATH, jars.get(0).getParentFile());

        for (File jar : jars) {
            jarList.getRoot().getChildren().add(new TreeItem<File>(jar));
        }

        update();
    }

    public void removeJar() {
        TreeItem<File> jar = jarList.getSelectionModel().getSelectedItem();
        if (jar == null) { return; }

        jarList.getRoot().getChildren().remove(jar);
        update();
    }

    private void update() {

        List<File> files = jarList.getRoot().getChildren().stream().map(node -> node.getValue())
                .collect(Collectors.toList());

        getChangeBus().broadcast(new ClasspathChange(files));
    }

}
