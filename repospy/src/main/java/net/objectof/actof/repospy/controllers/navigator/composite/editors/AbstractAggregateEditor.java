package net.objectof.actof.repospy.controllers.navigator.composite.editors;


import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import net.objectof.actof.repospy.controllers.navigator.composite.CompositeEntry;


public abstract class AbstractAggregateEditor extends AbstractComboboxEditor {

    private HBox buttonbox = new HBox(2);
    private BorderPane container = new BorderPane();

    private Image addImage = new Image(getClass().getResourceAsStream("icons/add.png"));
    private Image remImage = new Image(getClass().getResourceAsStream("icons/remove.png"));

    protected Button add = new Button("", new ImageView(addImage));
    protected Button remove = new Button("", new ImageView(remImage));

    public AbstractAggregateEditor(CompositeEntry entry, boolean editable) {
        super(entry, editable);

        buttonbox.getChildren().add(add);
        buttonbox.getChildren().add(remove);
        buttonbox.setPadding(new Insets(0, 0, 0, 2));

        container.setRight(buttonbox);
        container.setCenter(super.getWriteNode());

        add.setOnAction(event -> {
            doAdd(getValue());
        });

        remove.setOnAction(event -> {
            doRemove(getValue());
        });

    }

    @Override
    public Node getWriteNode() {
        return container;
    }

    protected abstract void doAdd(String text);

    protected abstract void doRemove(String text);

}