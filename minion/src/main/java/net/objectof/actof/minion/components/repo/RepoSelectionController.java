package net.objectof.actof.minion.components.repo;


import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import net.objectof.actof.common.controller.IActofUIController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.connectorui.ConnectionController;
import net.objectof.actof.connectorui.ConnectionSelectedChange;
import net.objectof.actof.minion.Minion;
import net.objectof.actof.widgets.StatusLight;
import net.objectof.actof.widgets.StatusLight.Status;
import net.objectof.model.Package;
import net.objectof.model.corc.IRepoHandler;


public class RepoSelectionController extends IActofUIController {

    private BorderPane ui;
    private ConnectionController conn;
    private StatusLight status;

    private Minion minion;


    @Override
    public void ready() {
        ui = new BorderPane();
        setNode(ui);

        Node center = conn.getNode();
        GridPane grid = new GridPane();
        center.setStyle("-fx-padding: 8; -fx-background-color: #fff; -fx-border-width: 1; -fx-border-color: #bbb;");
        grid.setAlignment(Pos.CENTER);
        grid.add(center, 0, 0);
        ui.setCenter(grid);


        status = new StatusLight("Not Connected");
        ui.setTop(status);

        getChangeBus().listen(ConnectionSelectedChange.class, selection -> {

            try {
                Package repo = selection.getConnector().getPackage();
                // TODO: Setting a handler from here will eventually be replaced
                minion.getServer().setHandler(new IRepoHandler(repo));
                status.setStatus(Status.GOOD, selection.getConnector().getPackageName());
            }
            catch (Exception e) {
                minion.getServer().setHandler(null);
                status.setStatus(Status.BAD, e.getMessage());
                e.printStackTrace();
            }
        });
    }


    public static RepoSelectionController load(ChangeController change) throws IOException {
        RepoSelectionController selection = new RepoSelectionController();
        selection.conn = ConnectionController.load(false, change);
        selection.setChangeBus(change);
        selection.ready();
        return selection;
    }

    public void setTopController(Minion minion) {
        this.minion = minion;
    }


    @Override
    public void initialize() throws Exception {
        // TODO Auto-generated method stub

    }




}
