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
import net.objectof.actof.widgets.KeyValuePane;
import net.objectof.actof.widgets.card.Card;


public class EntityCard extends Card {

    private IAggregateNode node;

    public EntityCard(RepoSpyTreeItem treeitem, RepoSpyController repospy) {
        this.node = (IAggregateNode) treeitem.getValue();

        setInnerPadding(new Insets(12));

        Hyperlink link = new Hyperlink(node.toString());
        link.setPadding(new Insets(0));
        link.setStyle("-fx-font-size: 13pt");
        link.setOnAction(event -> {
            repospy.getChangeBus().broadcast(new ResourceSelectedChange(treeitem));
        });
        setTitle(link);

        KeyValuePane fields = new KeyValuePane();
        fields.setPadding(new Insets(0, 0, 0, 20));
        setContent(fields);

        setDescription(RepoUtils.prettyPrint(node.getStereotype()));

        for (ILeafNode child : node.getLeaves()) {

            String keyString = child.getKey().toString();
            String valueString = "";

            switch (child.getStereotype()) {
                case BOOL:
                case FN:
                case INT:
                case MEDIA:
                case MOMENT:
                case NIL:
                case NUM:
                case REF:
                case TEXT:
                    valueString = RepoUtils.prettyPrint(child.getFieldValue());
                    break;

                case COMPOSED:
                case INDEXED:
                case MAPPED:
                case SET:
                    valueString = RepoUtils.prettyPrintKind(child.getKind());
                    break;

                default:
                    break;

            }

            if (valueString.length() > 100) {
                valueString = valueString.substring(0, 100);
            }

            fields.put(keyString, valueString);

        }

        ColumnConstraints constraints;
        constraints = new ColumnConstraints();
        constraints.setMinWidth(100);
        fields.getColumnConstraints().add(constraints);

    }
}
