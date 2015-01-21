package net.objectof.actof.widgets;


import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import org.controlsfx.dialog.Dialogs;


public class StatusLight extends AnchorPane {

    public enum Status {
        GOOD {

            @Override
            public String colour() {
                return "#4e9a06;";
            }
        },
        MAYBE {

            @Override
            public String colour() {
                return "#c4a000;";
            }
        },
        BAD {

            @Override
            public String colour() {
                return "#a40000;";
            }
        },
        OFF {

            @Override
            public String colour() {
                return "#555;";
            }
        };

        public String colour() {
            return "#fff;";
        }
    }

    private String style = "-fx-font-weight: bold; -fx-text-fill: #fff; -fx-font-family: Monospaced; -fx-padding: 3 3 3 3; ";
    private Label label;
    private Throwable throwable = null;

    public StatusLight() {
        this("");

        label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (throwable == null) { return; }
            Dialogs.create().showException(throwable);
        });

    }

    public StatusLight(String message) {
        label = new Label("Not Connected");
        label.setAlignment(Pos.CENTER);

        AnchorPane.setBottomAnchor(label, 0d);
        AnchorPane.setTopAnchor(label, 0d);
        AnchorPane.setLeftAnchor(label, 0d);
        AnchorPane.setRightAnchor(label, 0d);
        this.getChildren().add(label);

        setStatus(Status.OFF, message);
    }

    public void setStatus(Status status, Throwable t) {
        setStatus(status, t.getMessage());
        throwable = t;
    }

    public void setStatus(Status status, String message) {
        throwable = null;
        label.setText(message);
        label.setStyle(style + "-fx-background-color: " + status.colour());

    }

}
