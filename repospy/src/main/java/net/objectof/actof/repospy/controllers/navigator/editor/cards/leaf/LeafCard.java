package net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf;


import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;
import net.objectof.actof.widgets.card.Card;


public abstract class LeafCard extends Card {

    private ILeafNode entry;
    private boolean isUpdating = false;

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
