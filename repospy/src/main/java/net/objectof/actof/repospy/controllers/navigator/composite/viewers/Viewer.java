package net.objectof.actof.repospy.controllers.navigator.composite.viewers;


import javafx.scene.Node;
import net.objectof.actof.repospy.controllers.navigator.composite.CompositeEntry;
import net.objectof.model.Stereotype;


public interface Viewer {

    Node getNode();

    static Viewer createViewer(CompositeEntry entry) {

        Stereotype st = entry.getStereotype();

        switch (st) {

            case INT:
                return new IntegerViewer(entry);
            case NUM:
                return new RealViewer(entry);

            case BOOL:
            case COMPOSED:
            case FN:
            case INDEXED:
            case MAPPED:
            case MEDIA:
            case MOMENT:
            case NIL:
            case REF:
            case SET:
            case TEXT:
            default:
                return new SimpleViewer(entry);

        }

    }

}
