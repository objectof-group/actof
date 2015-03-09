package net.objectof.actof.repospy.controllers.navigator.composite.editors;


import javafx.scene.Node;
import net.objectof.actof.repospy.controllers.navigator.composite.CompositeEntry;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.aggregate.CompositeEditor;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.aggregate.IndexedEditor;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.map.MappedEditor;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.primitive.IntegerEditor;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.primitive.MomentEditor;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.primitive.RealEditor;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.primitive.ReferenceEditor;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.primitive.TextEditor;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.primitive.UnsupportedEditor;


public interface Editor {

    void focus();

    Node getNode();

    default boolean expand() {
        return true;
    }

    static Editor createEditor(CompositeEntry entry) {

        switch (entry.getStereotype()) {
            case BOOL:
                return new TextEditor(entry);
            case INDEXED:
                return new IndexedEditor(entry);
            case INT:
                return new IntegerEditor(entry);
            case MAPPED:
                return new MappedEditor(entry);
            case MOMENT:
                return new MomentEditor(entry);
            case NUM:
                return new RealEditor(entry);
            case REF:
                return new ReferenceEditor(entry);
            case TEXT:
                return new TextEditor(entry);
            case COMPOSED:
                return new CompositeEditor(entry);
            case SET:
            case MEDIA:
            case FN:

            default:
                return new UnsupportedEditor(entry);

        }
    }

}
