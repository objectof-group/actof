package net.objectof.actof.repospy.controllers.navigator.editor.cards;


import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.ColumnConstraints;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.ResourceSelectedChange;
import net.objectof.actof.repospy.controllers.navigator.treemodel.RepoSpyTreeItem;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.IAggregateNode;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;
import net.objectof.actof.widgets.PropertiesPane;
import net.objectof.actof.widgets.card.Card;


public class EntityCard extends Card {

    private IAggregateNode node;

    private String keyStyle = "-fx-text-fill: #999999;";
    private String valueStyle = "-fx-text-fill: #555555;";

    public EntityCard(RepoSpyTreeItem treeitem, RepoSpyController repospy) {
        this.node = (IAggregateNode) treeitem.getValue();

        Hyperlink link = new Hyperlink(node.toString());
        link.setStyle("-fx-font-size: 13pt");
        link.setOnAction(event -> {
            repospy.getChangeBus().broadcast(new ResourceSelectedChange(treeitem));
        });
        setTitle(link);

        PropertiesPane fields = new PropertiesPane();
        fields.setPadding(new Insets(0, 0, 0, 20));
        fields.setValueStyle(valueStyle);
        fields.setKeyStyle(keyStyle);
        setContent(fields);

        for (ILeafNode child : node.getLeaves(repospy)) {

            String keyString = child.key.toString();
            String valueString = RepoUtils.prettyPrint(child.getFieldValue());
            if (valueString.length() > 100) {
                valueString = valueString.substring(0, 100);
            }

            fields.addProperty(keyString, valueString);

        }

        ColumnConstraints constraints;
        constraints = new ColumnConstraints();
        constraints.setMinWidth(100);
        fields.getColumnConstraints().add(constraints);

    }
}
