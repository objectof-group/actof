package net.objectof.actof.minion.testgraph;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;


public class WindowBar extends HBox {

    private StringProperty title = new SimpleStringProperty("Title");

    private HBox buttonBox = new HBox();

    public WindowBar() {

        setFocusTraversable(true);

        setPadding(new Insets(3));
        setFillHeight(true);

        Label label = new Label();
        label.textProperty().bind(title);
        label.setAlignment(Pos.TOP_CENTER);
        AnchorPane.setTopAnchor(label, 0d);
        AnchorPane.setBottomAnchor(label, 0d);
        AnchorPane.setLeftAnchor(label, 0d);
        AnchorPane.setRightAnchor(label, 0d);
        AnchorPane titleAnchor = new AnchorPane(label);

        HBox.setHgrow(titleAnchor, Priority.ALWAYS);
        getChildren().add(titleAnchor);
        getChildren().add(buttonBox);

    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public StringProperty titleProperty() {
        return title;
    }

    public HBox getButtonBox() {
        return buttonBox;
    }
}
