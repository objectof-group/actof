package net.objectof.actof.minion.components.classpath;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import net.objectof.actof.common.controller.IActofUIController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.config.Env;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.minion.Settings;
import net.objectof.actof.minion.components.classpath.change.ClasspathChange;


public class ClasspathController extends IActofUIController {

    @FXML
    private ListView<File> jarList;

    private static final String SETTING_PATH = "net.objectof.actof.minion.classpath.path";

    @Override
    public void ready() {

        jarList.setCellFactory(list -> {
            return new TextFieldListCell<>(new StringConverter<File>() {

                @Override
                public String toString(File file) {
                    return file.getName();
                }

                @Override
                public File fromString(String string) {
                    return null;
                }
            });
        });

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
            jarList.getItems().add(jar);
        }

        update();
    }

    public void removeJar() {
        File jar = jarList.getSelectionModel().getSelectedItem();
        if (jar == null) { return; }
        jarList.getItems().remove(jar);
        update();
    }

    private void update() {
        List<File> files = jarList.getItems();
        getChangeBus().broadcast(new ClasspathChange(files));
    }

    public static ClasspathController load(ChangeController changes) throws IOException {
        return FXUtil.load(ClasspathController.class, "Classpath.fxml", changes);
    }

}
