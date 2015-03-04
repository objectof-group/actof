package net.objectof.actof.minion.components.handlers.ui;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.objectof.actof.common.controller.IActofUIController;
import net.objectof.actof.common.controller.change.IChangeController;
import net.objectof.actof.common.util.FXUtil;


public class HandlerUI extends IActofUIController {

    @FXML
    private Label label;
    @FXML
    private ImageView icon;

    @Override
    public void initialize() {
        // TODO Auto-generated method stub

    }

    @Override
    public void ready() {
        // TODO Auto-generated method stub

    }

    public String getText() {
        return label.getText();
    }

    public void setText(String text) {
        label.setText(text);
    }

    public void setIcon(File file) {
        try {
            icon.setImage(new Image(new FileInputStream(file)));
        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static HandlerUI load() throws IOException {
        return FXUtil.load(HandlerUI.class, "HandlerUI.fxml", new IChangeController());
    }

}
