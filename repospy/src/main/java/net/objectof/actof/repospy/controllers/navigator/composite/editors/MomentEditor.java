package net.objectof.actof.repospy.controllers.navigator.composite.editors;


import net.objectof.actof.repospy.controllers.navigator.composite.CompositeEntry;
import net.objectof.model.impl.IMoment;


public class MomentEditor extends TextEditor {

    public MomentEditor(CompositeEntry entry) {
        super(entry);
        IMoment moment = (IMoment) entry.getFieldValue();
        if (moment != null) {
            field.setText(moment.toString());
        }

    }

    @Override
    protected boolean validate(String text) {

        try {
            new IMoment(text);
            return true;
        }
        catch (IllegalArgumentException e) {
            return false;
        }
    }

}
