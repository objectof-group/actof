package net.objectof.actof.repospy.controllers.navigator.editor.cards;


import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.ColumnConstraints;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.ResourceSelectedChange;
import net.objectof.actof.repospy.controllers.navigator.treemodel.RepoSpyTreeItem;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.IKindNode;
import net.objectof.actof.widgets.PropertiesPane;
import net.objectof.actof.widgets.card.Card;
import net.objectof.model.Kind;


public class KindCard extends Card {

    private IKindNode node;

    public KindCard(RepoSpyTreeItem treeitem, RepoSpyController repospy) {
        this.node = (IKindNode) treeitem.getValue();

        Hyperlink link = new Hyperlink(node.getEntityKind());
        link.setStyle("-fx-font-size: 13pt");
        link.setOnAction(event -> {
            repospy.getChangeBus().broadcast(new ResourceSelectedChange(treeitem));
        });
        setTitle(link);

        PropertiesPane fields = new PropertiesPane();
        fields.setPadding(new Insets(0, 0, 0, 20));
        setContent(fields);

        for (Kind<?> part : node.getKind().getParts()) {
            String keyString = part.getComponentName();
            String[] keyParts = keyString.split("\\.");
            keyString = keyParts[keyParts.length - 1];
            String valueString = RepoUtils.prettyPrint(part.getStereotype());
            fields.addProperty(keyString, valueString);
        }

        ColumnConstraints constraints;
        constraints = new ColumnConstraints();
        constraints.setMinWidth(100);
        fields.getColumnConstraints().add(constraints);

    }
}
