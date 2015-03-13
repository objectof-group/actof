package net.objectof.actof.repospy.controllers.navigator.editor.cards;


import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.ResourceSelectedChange;
import net.objectof.actof.repospy.controllers.navigator.treemodel.RepoSpyTreeItem;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.IAggregateNode;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;
import net.objectof.actof.widgets.card.Card;


public class EntityCard extends Card {

    private IAggregateNode node;

    private String style = "-fx-text-fill: #777777;";

    public EntityCard(RepoSpyTreeItem treeitem, RepoSpyController repospy) {
        this.node = (IAggregateNode) treeitem.getValue();

        Hyperlink link = new Hyperlink(node.toString());
        link.setStyle("-fx-font-size: 13pt");
        link.setOnAction(event -> {
            repospy.getChangeBus().broadcast(new ResourceSelectedChange(treeitem));
        });
        setTitle(link);

        GridPane fields = new GridPane();
        fields.setHgap(10);
        fields.setPadding(new Insets(0, 0, 0, 20));
        setContent(fields);

        int row = 0;
        for (ILeafNode child : node.getLeaves(repospy)) {

            String keyString = child.key.toString();
            String valueString = RepoUtils.prettyPrint(child.getFieldValue());
            if (valueString.length() > 100) {
                valueString = valueString.substring(0, 100);
            }

            Label keyLabel = new Label(keyString);
            Label valueLabel = new Label(valueString);

            keyLabel.setStyle(style);
            valueLabel.setStyle(style);

            fields.add(keyLabel, 0, row);
            fields.add(valueLabel, 1, row);
            row++;
        }

        ColumnConstraints constraints;

        constraints = new ColumnConstraints();
        constraints.setMinWidth(100);
        fields.getColumnConstraints().add(constraints);

    }
}
