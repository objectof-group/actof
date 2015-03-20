package net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf;


import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import net.objectof.actof.common.icons.ActofIcons;
import net.objectof.actof.common.icons.ActofIcons.Icon;
import net.objectof.actof.common.icons.ActofIcons.Size;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.changes.EditingChange;
import net.objectof.actof.repospy.changes.FieldChange;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;
import net.objectof.actof.widgets.card.Card;


public abstract class LeafCard extends Card {

    private ILeafNode entry;
    private boolean isUpdating = false;
    private Button undo;

    public LeafCard(ILeafNode entry) {
        this.entry = entry;

        // add a listener to the LeafNode, when the value changes, update the
        // control/UI. Set the isUpdating flag so that UI component doesn't
        // think the user modified the value
        entry.addListener(change -> {
            updateFromEntry();
        });
        setTitle(getLeafTitle());
        setDescription(RepoUtils.prettyPrint(entry.getStereotype()));

        Node desc = getDescription();
        AnchorPane.setTopAnchor(desc, 0d);
        AnchorPane.setBottomAnchor(desc, 0d);
        AnchorPane.setLeftAnchor(desc, 0d);
        AnchorPane.setRightAnchor(desc, 0d);
        desc = new AnchorPane(desc);

        undo = new Button("", ActofIcons.getIconView(Icon.UNDO, Size.BUTTON));
        undo.getStyleClass().add("tool-bar-button");
        undo.setOnAction(action -> {
            undoable().undo();
        });
        undo.setVisible(false);
        HBox box = new HBox(10, undo, desc);
        setDescription(box);

        entry.getController().getChangeBus().listen(FieldChange.class, change -> {
            if (undo.isFocused()) {
                getTitle().requestFocus();
            }
            undo.setVisible(undoable() != null);
        });

    }

    private FieldChange undoable() {
        for (EditingChange edit : getEntry().getController().history.getChanges()) {
            if (!(edit instanceof FieldChange)) {
                continue;
            }
            FieldChange fieldChange = (FieldChange) edit;
            if (fieldChange.getLeafNode().equals(entry)) { return fieldChange; }
        }
        return null;
    }

    protected String getLeafTitle() {
        return entry.getKey().toString();
    }

    protected ILeafNode getEntry() {
        return entry;
    }

    public static LeafCard createEditor(ILeafNode entry) {

        switch (entry.getStereotype()) {
            case BOOL:
                return new BooleanCard(entry);
            case INT:
                return new IntegerCard(entry);
            case MOMENT:
                return new MomentCard(entry);
            case NUM:
                return new RealCard(entry);
            case REF:
                return new ReferenceCard(entry);
            case TEXT:
                return new TextCard(entry);

            case MAPPED:
            case INDEXED:
            case COMPOSED:
            case SET:
                return new EntryLinkCard(entry);

            case MEDIA:
            case FN:
            default:
                return new UnsupportedCard(entry);

        }
    }

    public void updateFromEntry() {
        try {
            isUpdating = true;

            if (getEntry().getFieldValue() == null) {
                setShadowColour("#802020");
            } else {
                setShadowColour("#777777");

            }

            updateUIFromEntry();
        }
        finally {
            isUpdating = false;
        }

    }

    public void updateUIFromEntry() {}

    public boolean isUpdating() {
        return isUpdating;
    }

}
