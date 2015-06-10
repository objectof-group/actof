package net.objectof.actof.porter.ui.porter;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import net.objectof.actof.common.controller.IActofUIController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.icons.ActofIcons;
import net.objectof.actof.common.icons.ActofIcons.Icon;
import net.objectof.actof.common.icons.ActofIcons.Size;
import net.objectof.actof.common.util.ActofUtil;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.porter.Porter;
import net.objectof.actof.porter.rules.Rule;
import net.objectof.actof.porter.ui.connector.ConnectorChooserButton;
import net.objectof.actof.porter.ui.operations.OperationUI;
import net.objectof.actof.porter.ui.rule.RuleUI;
import net.objectof.actof.widgets.masonry.MasonryPane;
import net.objectof.actof.widgets.masonry.MasonryPane.Layout;
import net.objectof.connector.ConnectorException;
import net.objectof.model.Package;


public class PorterUIController extends IActofUIController {

    @FXML
    public HBox connectors, buttonBox;

    @FXML
    private ScrollPane ruleScroller;

    @FXML
    private Button newButton, openButton, saveButton, opButton;

    @FXML
    private TitledPane packagesPane;

    private MasonryPane rulesPane;

    private Button addButton = new Button(null, ActofIcons.getIconView(Icon.ADD, Size.BUTTON));

    private ObservableList<RuleUI> rules = FXCollections.observableArrayList();

    private ConnectorChooserButton connection1, connection2;

    private FileChooser fileChooser = new FileChooser();
    {
        fileChooser.setTitle("Open Project");
        ExtensionFilter filter = new ExtensionFilter("Porter Project Files", "*.ppf");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setSelectedExtensionFilter(filter);
    }

    @Override
    public void ready() {

        newButton.setGraphic(ActofIcons.getCustomIcon(getClass(), "../icons/document-new.png"));
        openButton.setGraphic(ActofIcons.getCustomIcon(getClass(), "../icons/document-open.png"));
        saveButton.setGraphic(ActofIcons.getCustomIcon(getClass(), "../icons/document-save.png"));
        opButton.setGraphic(ActofIcons.getCustomIcon(getClass(), "../icons/operations.png"));

        rulesPane = new MasonryPane(500, Layout.GRID);
        rulesPane.setSpacing(6);
        rulesPane.setPadding(new Insets(6));
        ruleScroller.setContent(rulesPane);

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

    public void onNewProject() {
        rules.clear();
        rules.add(new RuleUI(getChangeBus(), this));
    }

    public void onOpenProject() throws FileNotFoundException {

        File target = fileChooser.showOpenDialog(null);
        if (target == null) { return; }
        fileChooser.setInitialDirectory(target.getParentFile());

        Scanner s = new Scanner(target);
        s = s.useDelimiter("\\Z");
        String data = s.next();
        s.close();

        fromMap((Map<String, Object>) ActofUtil.deserialize(data));
    }

    public void onSaveProject() throws IOException {
        String data = ActofUtil.serialize(toMap());

        File target = fileChooser.showSaveDialog(null);
        if (target == null) { return; }
        fileChooser.setInitialDirectory(target.getParentFile());

        Writer w = new OutputStreamWriter(new FileOutputStream(target));
        w.write(data);
        w.close();

    }

    public void onOpButton() throws IOException {
        OperationUI ops = OperationUI.load(getChangeBus());
        Scene scene = new Scene((Parent) ops.getNode());
        Stage stage = new Stage();
        stage.setTitle("Custom Operations Editor");
        stage.setScene(scene);
        stage.show();
    }

    private Map<String, Object> toMap() {
        return new HashMap<String, Object>() {

            {
                put("rules", rules.stream().map(RuleUI::toMap).collect(Collectors.toList()));
            }
        };
    }

    private void fromMap(Map<String, Object> map) {
        rules.clear();
        List<Map<String, Object>> datalist = (List<Map<String, Object>>) map.get("rules");
        datalist.stream().forEach(data -> {
            RuleUI r = new RuleUI(getChangeBus(), this);
            r.fromMap(data);
            rules.add(r);
        });
    }

    private void layoutRules() {
        rulesPane.getChildren().setAll(rules);
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

    public void start() {
        // hide title component of packages titledpane
        Pane title = (Pane) packagesPane.lookup(".title");
        if (title != null) {
            title.setVisible(false);
            title.setMinHeight(0);
            title.setPrefHeight(0);
            title.setMaxHeight(0);
        }
    }

}
