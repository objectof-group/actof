package net.objectof.actof.porter.ui.rule;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.icons.ActofIcons;
import net.objectof.actof.common.icons.ActofIcons.Icon;
import net.objectof.actof.common.icons.ActofIcons.Size;
import net.objectof.actof.porter.rules.Rule;
import net.objectof.actof.porter.rules.RuleBuilder;
import net.objectof.actof.porter.ui.porter.PorterUIController;
import net.objectof.actof.porter.ui.rule.condition.ConditionUI;
import net.objectof.actof.widgets.card.Card;


public class RuleUI extends Card {

    private BorderPane contents = new BorderPane();
    private VBox conditionsBox = new VBox();
    private HBox buttonBox = new HBox();
    private RuleLabel title = new RuleLabel("Rule");
    private Button removeRule, up, down;
    private Button addCondition;
    private HBox descBox = new HBox(6);

    private PorterUIController porterUI;
    private ChangeController changes;
    private ObservableList<ConditionUI> conditions = FXCollections.observableArrayList();

    public RuleUI(ChangeController changes, PorterUIController porterUI) {
        this.porterUI = porterUI;
        this.changes = changes;
        conditions.addListener((Observable change) -> layoutConditions());

        try {
            conditions.add(ConditionUI.load(changes, this));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        ImageView addIcon = new ImageView(new Image(RuleUI.class.getResourceAsStream("icons/add-symbolic-12.png")));
        addCondition = new Button(null, addIcon);
        addCondition.getStyleClass().add("tool-bar-button");
        addCondition.setOnAction(event -> {
            try {
                conditions.add(ConditionUI.load(changes, this));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        buttonBox.getChildren().add(addCondition);
        buttonBox.setPadding(new Insets(6, 0, 0, 0));

        conditionsBox.setSpacing(6);

        contents.setCenter(conditionsBox);
        contents.setBottom(buttonBox);

        removeRule = new Button(null, ActofIcons.getIconView(Icon.REMOVE, Size.BUTTON));
        removeRule.getStyleClass().add("tool-bar-button");
        removeRule.setOnAction(event -> {
            porterUI.getRules().remove(this);
        });

        up = new Button(null, ActofIcons.getCustomIcon(RuleUI.class, "icons/go-up.png"));
        down = new Button(null, ActofIcons.getCustomIcon(RuleUI.class, "icons/go-down.png"));
        up.getStyleClass().add("tool-bar-button");
        down.getStyleClass().add("tool-bar-button");
        up.setOnAction(event -> onUp());
        down.setOnAction(event -> onDown());
        descBox.getChildren().setAll(up, down, removeRule);
        descBox.setVisible(false);

        setContent(contents);
        setTitle(title);
        setDescription(descBox);

        setOnMouseEntered(event -> descBox.setVisible(true));
        setOnMouseExited(event -> descBox.setVisible(false));

        VBox.setVgrow(this, Priority.ALWAYS);
    }

    private void layoutConditions() {
        conditionsBox.getChildren().setAll(conditions.stream().map(ConditionUI::getNode).collect(Collectors.toList()));
    }

    public ObservableList<ConditionUI> getConditions() {
        return conditions;
    }

    public Map<String, Object> toMap() {
        return new HashMap<String, Object>() {

            {
                put("title", title.getText());
                put("conditions", conditions.stream().map(ConditionUI::toMap).collect(Collectors.toList()));
            }
        };
    }

    public void fromMap(Map<String, Object> map) {

        conditions.clear();
        title.setText(map.get("title").toString());
        List<Map<String, Object>> datalist = (List<Map<String, Object>>) map.get("conditions");
        datalist.stream().forEach(data -> {
            try {
                ConditionUI condition = ConditionUI.load(changes, this);
                condition.fromMap(data);
                conditions.add(condition);
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

    }

    public Rule generateRule() {
        RuleBuilder rb = RuleBuilder.start();
        conditions.stream().forEach(c -> c.apply(rb));
        return rb.build();
    }

    public void onUp() {
        int index = porterUI.getRules().indexOf(this);
        index = Math.max(0, index - 1);
        porterUI.getRules().remove(this);
        porterUI.getRules().add(index, this);
    }

    public void onDown() {
        int index = porterUI.getRules().indexOf(this);
        index = Math.min(porterUI.getRules().size() - 1, index + 1);
        porterUI.getRules().remove(this);
        porterUI.getRules().add(index, this);
    }
}
