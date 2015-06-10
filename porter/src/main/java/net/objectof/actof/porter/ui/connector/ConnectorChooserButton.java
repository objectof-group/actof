package net.objectof.actof.porter.ui.connector;


import javafx.scene.control.Button;
import javafx.stage.Window;
import net.objectof.actof.connectorui.ConnectionController;
import net.objectof.connector.Connector;


public class ConnectorChooserButton extends Button {

    private Connector connector = null;

    public ConnectorChooserButton(Window parent) {

        setPrefWidth(300);
        setText("(No Package)");

        this.setOnMouseClicked(event -> {
            try {
                Connector selectedConnector = ConnectionController.showDialog(parent, false);
                if (selectedConnector == null) { return; }
                connector = selectedConnector;

                setText(connector.getPackageName());

            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    public Connector getConnector() {
        return connector;
    }

}
