package net.objectof.actof.porter.ui.porter;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.objectof.actof.common.controller.IActofUIController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.icons.ActofIcons;
import net.objectof.actof.common.icons.ActofIcons.Icon;
import net.objectof.actof.common.icons.ActofIcons.Size;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.porter.Porter;
import net.objectof.actof.porter.rules.Rule;
import net.objectof.actof.porter.ui.rule.RuleUI;
import net.objectof.connector.ConnectorException;
import net.objectof.model.Package;


public class PorterUIController extends IActofUIController {

    @FXML
    public HBox connectors;

    @FXML
    public VBox rulesBox;

    @FXML
    private Button portButton;

    private HBox buttonBox = new HBox(6);
    private Button addButton = new Button(null, ActofIcons.getIconView(Icon.ADD, Size.BUTTON));

    private ObservableList<RuleUI> rules = FXCollections.observableArrayList();

    private ConnectorChooserButton connection1, connection2;

    @Override
    public void ready() {

        addButton.getStyleClass().add("tool-bar-button");
        addButton.setOnAction(event -> {
            rules.add(new RuleUI(getChangeBus(), this));
        });
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().add(addButton);

        rules.addListener((Observable change) -> {
            layoutRules();
        });

        Label arrow = new Label("\u279E");
        arrow.setStyle("-fx-font-size: 200%;");

        connection1 = new ConnectorChooserButton(null);
        connection2 = new ConnectorChooserButton(null);
        connectors.getChildren().add(1, connection1);
        connectors.getChildren().add(2, arrow);
        connectors.getChildren().add(3, connection2);

        RuleUI rule = new RuleUI(getChangeBus(), this);
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
        rulesBox.getChildren().add(buttonBox);
    }

    public static PorterUIController load(ChangeController changes) throws IOException {
        return FXUtil.load(PorterUIController.class, "PorterUI.fxml", changes);
    }

    public List<Rule> generateRules() {
        return rules.stream().map(ruleui -> ruleui.generateRule()).collect(Collectors.toList());
    }

    public ObservableList<RuleUI> getRules() {
        return rules;
    }

}
