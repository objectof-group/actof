package net.objectof.actof.repospy.controllers.review;


import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.icons.ActofIcons;
import net.objectof.actof.common.icons.ActofIcons.Icon;
import net.objectof.actof.common.icons.ActofIcons.Size;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.changes.EditingChange;
import net.objectof.actof.repospy.changes.EntityCreatedChange;
import net.objectof.actof.repospy.changes.FieldChange;
import net.objectof.actof.repospy.changes.HistoryChange;
import net.objectof.actof.repospy.controllers.history.HistoryController;
import net.objectof.actof.widgets.PropertiesPane;
import net.objectof.actof.widgets.card.Card;
import net.objectof.actof.widgets.card.CardsPane;
import net.objectof.aggr.Aggregate;


public class ReviewController extends CardsPane {

    private HistoryController history;
    private ChangeController changes;

    public ReviewController(HistoryController history, ChangeController changes) {
        this.history = history;
        this.changes = changes;
        setPrefSize(400, 400);
        changes.listen(HistoryChange.class, this::updateUI);
        updateUI();
    }

    private void updateUI() {

        getChildren().clear();
        for (EditingChange change : history.get()) {

            Card card = new Card();
            card.setTitle(change.getName());
            card.setDescription(RepoUtils.prettyPrintStereotype(change.getStereotype()));

            if (change instanceof FieldChange) {

                Node desc = card.getDescription();
                AnchorPane.setTopAnchor(desc, 0d);
                AnchorPane.setBottomAnchor(desc, 0d);
                AnchorPane.setLeftAnchor(desc, 0d);
                AnchorPane.setRightAnchor(desc, 0d);
                desc = new AnchorPane(desc);

                Button revert = new Button("", ActofIcons.getIconView(Icon.UNDO, Size.BUTTON));
                revert.setOnAction(event -> {
                    doRevert(change);
                });

                HBox box = new HBox(10, desc, revert);
                card.setDescription(box);
            }

            PropertiesPane fields = new PropertiesPane();
            fields.setPadding(new Insets(0, 0, 0, 20));
            fields.addProperty("Old", RepoUtils.prettyPrint(change.oldValue()));
            fields.addProperty("New", RepoUtils.prettyPrint(change.newValue()));
            card.setContent(fields);

            getChildren().add(card);
        }
    }

    private void doRevert(EditingChange change) {

        if (change instanceof FieldChange) {
            FieldChange fieldChange = (FieldChange) change;
            Aggregate<Object, Object> agg = (Aggregate<Object, Object>) fieldChange.getLeafnode().parent;
            agg.set(fieldChange.getLeafnode().key, fieldChange.oldValue());
            changes.broadcast(new FieldChange(fieldChange.oldValue(), fieldChange.oldValue(), fieldChange.getLeafnode()));
        } else if (change instanceof EntityCreatedChange) {
            EntityCreatedChange createdChange = (EntityCreatedChange) change;

        }

    }

    class StyledTableCell<S, T> extends TableCell<S, T> {

        private String style;

        public StyledTableCell(String style) {
            this.style = style;
        }

        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);

            setStyle(style);

            if (item == null || empty) {
                setText(null);
            } else {
                setText(item.toString());
            }
        }
    }

}
