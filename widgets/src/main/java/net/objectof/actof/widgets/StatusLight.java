package net.objectof.actof.widgets;


import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;


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

    public StatusLight() {
        this("");
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

    public void setStatus(Status status, String message) {
        label.setText(message);
        label.setStyle(style + "-fx-background-color: " + status.colour());

    }

}
