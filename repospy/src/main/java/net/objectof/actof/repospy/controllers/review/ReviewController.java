package net.objectof.actof.repospy.controllers.review;


import javafx.geometry.Insets;
import javafx.scene.control.TableCell;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.changes.EditingChange;
import net.objectof.actof.repospy.changes.HistoryChange;
import net.objectof.actof.repospy.controllers.history.HistoryController;
import net.objectof.actof.widgets.PropertiesPane;
import net.objectof.actof.widgets.card.Card;
import net.objectof.actof.widgets.card.CardsPane;


public class ReviewController extends CardsPane {

    private HistoryController history;

    public ReviewController(HistoryController history, ChangeController changes) {
        this.history = history;
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
            PropertiesPane fields = new PropertiesPane();
            fields.setPadding(new Insets(0, 0, 0, 20));
            fields.addProperty("Old", RepoUtils.prettyPrint(change.oldValue()));
            fields.addProperty("New", RepoUtils.prettyPrint(change.newValue()));
            card.setContent(fields);
            getChildren().add(card);
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
