package net.objectof.actof.repospy.controllers.navigator.editor.layout;


import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.treemodel.IAggregateNode;
import net.objectof.actof.widgets.card.CardsPane;


public abstract class AbstractView extends BorderPane {

    protected CardsPane cards = new CardsPane();
    protected ScrollPane scroll = new ScrollPane(cards);
    protected IAggregateNode entry;
    protected RepoSpyController repospy;

    public AbstractView(IAggregateNode entry, RepoSpyController repospy) {
        this.entry = entry;
        this.repospy = repospy;

        scroll.setStyle("-fx-background-color:transparent;");
        scroll.setFitToWidth(true);
        scroll.setContent(cards);

        setCenter(scroll);

    }

    protected abstract void updateUI();

}
