package net.objectof.actof.repospy.controllers.navigator.editor.layout;


import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.treemodel.RepoSpyTreeItem;
import net.objectof.actof.widgets.card.CardsPane;


public abstract class AbstractLayout extends BorderPane {

    protected CardsPane cards = new CardsPane();
    protected ScrollPane scroll = new ScrollPane(cards);
    protected RepoSpyController repospy;
    protected RepoSpyTreeItem treeitem;

    public AbstractLayout(RepoSpyTreeItem treeitem, RepoSpyController repospy) {

        this.repospy = repospy;
        this.treeitem = treeitem;

        scroll.setStyle("-fx-background-color:transparent;");
        scroll.setFitToWidth(true);
        scroll.setContent(cards);

        setCenter(scroll);

    }

    protected abstract void updateUI();

}
