package net.objectof.actof.repospy.controllers.navigator.editor.cards;


import net.objectof.actof.repospy.controllers.navigator.treemodel.ILeafNode;
import net.objectof.actof.widgets.card.Card;


public abstract class LeafCard extends Card {

    private ILeafNode entry;

    public LeafCard(ILeafNode entry) {
        this.entry = entry;
        setTitle(getLeafTitle());
        setDescription(entry.getStereotype().toString());
    }

    protected String getLeafTitle() {
        String title = entry.getKey().toString();
        return title.substring(0, 1).toUpperCase() + title.substring(1);
    }

    protected ILeafNode getEntry() {
        return entry;
    }

    public static LeafCard createEditor(ILeafNode entry) {

        switch (entry.getStereotype()) {
            case BOOL:
                return new TextCard(entry);
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

}
