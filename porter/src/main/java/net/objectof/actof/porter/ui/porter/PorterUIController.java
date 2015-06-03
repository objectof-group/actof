package net.objectof.actof.porter.ui.porter;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.objectof.actof.common.controller.IActofUIController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.connectorui.ConnectionController;
import net.objectof.actof.porter.Porter;
import net.objectof.actof.porter.rules.Rule;
import net.objectof.actof.porter.ui.rule.RuleUI;
import net.objectof.connector.ConnectorException;
import net.objectof.model.Package;


public class PorterUIController extends IActofUIController {

    @FXML
    public HBox connectors;
    public VBox rulesBox;

    private ObservableList<RuleUI> rules = FXCollections.observableArrayList();

    private ConnectionController connection1, connection2;

    @Override
    public void ready() {

        rules.addListener((Observable change) -> {
            layoutRules();
        });

        try {
            connection1 = ConnectionController.load(getChangeBus());
            connection2 = ConnectionController.load(getChangeBus());
            connectors.getChildren().add(1, connection1.getNode());
            connectors.getChildren().add(3, connection2.getNode());
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RuleUI rule = new RuleUI(getChangeBus());
        rules.add(rule);

    }

    @Override
    protected void initialize() throws Exception {
        // TODO Auto-generated method stub

    }

    public void onPort() {

        try {

            Package fromPackage = connection1.getConnector().getPackage();
            Package toPackage = connection2.getConnector().getPackage();

            Porter porter = new Porter();
            porter.getRules().addAll(generateRules());
            porter.port(fromPackage, toPackage);

        }
        catch (ConnectorException e) {
            e.printStackTrace();
        }

    }

    private void layoutRules() {
        rulesBox.getChildren().setAll(rules);
    }

    public static PorterUIController load(ChangeController changes) throws IOException {
        return FXUtil.load(PorterUIController.class, "PorterUI.fxml", changes);
    }

    public List<Rule> generateRules() {
        return rules.stream().map(ruleui -> ruleui.generateRule()).collect(Collectors.toList());
    }

}
