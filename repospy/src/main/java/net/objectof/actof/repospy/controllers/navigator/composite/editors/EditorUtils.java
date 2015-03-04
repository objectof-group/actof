package net.objectof.actof.repospy.controllers.navigator.composite.editors;


import javafx.scene.control.Cell;
import net.objectof.actof.repospy.controllers.navigator.composite.CompositeEntry;
import net.objectof.model.Stereotype;


public class EditorUtils {

    public static boolean isStereotypeSupported(Stereotype st) {
        switch (st) {
            case BOOL:
            case INT:
            case NUM:
            case TEXT:
            case REF:
            case MOMENT:
            case MAPPED:
            case INDEXED:
                return true;

            case COMPOSED: // editing supported through child nodes, not
                           // directly
            case FN:
            case MEDIA:
            case SET:
                return false;
            default:
                return false;
        }
    }

    public static Editor createConfiguredEditor(Cell<CompositeEntry> cell) {

        CompositeEntry entry = cell.getItem();
        Editor editor = createEditor(entry);

        editor.setOnComplete(newValue -> {
            entry.userInputValue(newValue);
            cell.commitEdit(entry);
        });

        editor.setOnCancel(() -> cell.cancelEdit());

        editor.setOnModify(newValue -> entry.addChangeHistory(newValue));

        return editor;
    }

    public static Editor createEditor(CompositeEntry entry) {

        switch (entry.getStereotype()) {
            case BOOL:
                return new TextEditor(entry);
            case COMPOSED:
                throw new UnsupportedOperationException();
            case FN:
                throw new UnsupportedOperationException();
            case INDEXED:
                return new IndexedEditor(entry);
            case INT:
                return new IntegerEditor(entry);
            case MAPPED:
                // throw new UnsupportedOperationException();
                return new MappedEditor(entry);
            case MEDIA:
                throw new UnsupportedOperationException();
            case MOMENT:
                return new MomentEditor(entry);
            case NUM:
                return new RealEditor(entry);
            case REF:
                // return new TextEditor(entry);
                return new ReferenceEditor(entry);
            case SET:
                throw new UnsupportedOperationException();
            case TEXT:
                return new TextEditor(entry);
            default:
                throw new UnsupportedOperationException();

        }
    }

}
