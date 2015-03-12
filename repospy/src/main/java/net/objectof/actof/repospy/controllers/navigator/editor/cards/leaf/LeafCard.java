package net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf;


import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;
import net.objectof.actof.widgets.card.Card;


public abstract class LeafCard extends Card {

    private ILeafNode entry;

    public LeafCard(ILeafNode entry, boolean capitalize) {
        this.entry = entry;
        setTitle(getLeafTitle(capitalize));
        setDescription(entry.getStereotype().toString());
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
                return new TextCard(entry, capitalize);
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

}
