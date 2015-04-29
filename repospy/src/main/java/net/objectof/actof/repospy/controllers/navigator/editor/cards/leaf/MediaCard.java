package net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;


public class MediaCard extends LeafCard {

    private Button download, upload;

    public MediaCard(ILeafNode entry) {
        super(entry);

        ImageView downloadIcon = new ImageView(new Image(MediaCard.class.getResourceAsStream("icons/download.png")));
        download = new Button("", downloadIcon);
        download.getStyleClass().add("tool-bar-button");
        download.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            File target = chooser.showSaveDialog(null);
            if (target == null) { return; }
            try {
                OutputStream fileout = new FileOutputStream(target);
                fileout.write((byte[]) entry.getFieldValue());
                fileout.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });

        ImageView uploadIcon = new ImageView(new Image(MediaCard.class.getResourceAsStream("icons/upload.png")));
        upload = new Button("", uploadIcon);
        upload.getStyleClass().add("tool-bar-button");
        upload.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            File target = chooser.showOpenDialog(null);
            if (target == null) { return; }
            try {
                byte[] bytes = Files.readAllBytes(target.toPath());
                entry.setFieldValue(bytes);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });

        descBox.getChildren().add(0, upload);
        descBox.getChildren().add(0, download);
    }
}
