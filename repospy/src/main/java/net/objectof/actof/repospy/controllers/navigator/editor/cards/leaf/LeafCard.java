package net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf;


import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;
import net.objectof.actof.widgets.card.Card;


public abstract class LeafCard extends Card {

    private ILeafNode entry;
    private boolean isUpdating = false;

    public LeafCard(ILeafNode entry, boolean capitalize) {
        this.entry = entry;

        // add a listener to the LeafNode, when the value changes, update the
        // control/UI. Set the isUpdating flag so that UI component doesn't
        // think the user modified the value
        entry.addListener(change -> {
            updateFromEntry();
        });
        setTitle(getLeafTitle(capitalize));
        setDescription(RepoUtils.prettyPrintStereotype(entry.getStereotype()));
    }

    protected String getLeafTitle(boolean capitalize) {
        String title = entry.getKey().toString();
        if (!capitalize) { return title; }
        if (title.length() == 0) { return ""; }
        return title.substring(0, 1).toUpperCase() + title.substring(1);
    }

    protected ILeafNode getEntry() {
        return entry;
    }

    public static LeafCard createEditor(ILeafNode entry) {
        return createEditor(entry, true);
    }

    public static LeafCard createEditor(ILeafNode entry, boolean capitalize) {

        switch (entry.getStereotype()) {
            case BOOL:
                return new BooleanCard(entry, capitalize);
            case INT:
                return new IntegerCard(entry, capitalize);
            case MOMENT:
                return new MomentCard(entry, capitalize);
            case NUM:
                return new RealCard(entry, capitalize);
            case REF:
                return new ReferenceCard(entry, capitalize);
            case TEXT:
                return new TextCard(entry, capitalize);

            case MAPPED:
            case INDEXED:
            case COMPOSED:
            case SET:
                return new EntryLinkCard(entry, capitalize);

            case MEDIA:
            case FN:
            default:
                return new UnsupportedCard(entry, capitalize);

        }
    }

    public void updateFromEntry() {
        try {
            isUpdating = true;
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
