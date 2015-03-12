package net.objectof.actof.repospy.controllers.navigator.editor.cards;


import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.ResourceSelectedChange;
import net.objectof.actof.repospy.controllers.navigator.treemodel.RepoSpyTreeItem;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.IKindNode;
import net.objectof.actof.widgets.card.Card;
import net.objectof.model.Kind;


public class KindCard extends Card {

    private IKindNode node;
    private String style = "-fx-text-fill: #777777;";

    public KindCard(RepoSpyTreeItem treeitem, RepoSpyController repospy) {
        this.node = (IKindNode) treeitem.getValue();

        Hyperlink link = new Hyperlink(node.getEntityKind());
        link.setStyle("-fx-font-size: 13pt");
        link.setOnAction(event -> {
            repospy.getChangeBus().broadcast(new ResourceSelectedChange(treeitem));
        });
        setTitle(link);

        GridPane fields = new GridPane();
        fields.setHgap(10);
        fields.setPadding(new Insets(0, 0, 0, 20));
        setContent(fields);

        node.getKind().getParts();

        int row = 0;
        for (Kind<?> part : node.getKind().getParts()) {

            String keyString = part.getComponentName();
            String[] keyParts = keyString.split("\\.");
            keyString = keyParts[keyParts.length - 1];

            String valueString = RepoUtils.resToString(part.getStereotype());
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

    }
}
