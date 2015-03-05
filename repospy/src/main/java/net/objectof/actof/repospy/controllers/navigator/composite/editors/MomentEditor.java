package net.objectof.actof.repospy.controllers.navigator.composite.editors;


import java.util.Calendar;
import java.util.TimeZone;

import javafx.scene.Node;
import jfxtras.scene.control.CalendarPicker;
import net.objectof.actof.repospy.controllers.navigator.composite.CompositeEntry;
import net.objectof.model.impl.IMoment;


public class MomentEditor extends AbstractEditor {

    private CalendarPicker picker;

    public MomentEditor(CompositeEntry entry) {
        super(entry);

        TimeZone zulu = TimeZone.getTimeZone("GMT");
        IMoment moment = (IMoment) entry.getFieldValue();
        if (moment == null) {
            moment = new IMoment();
        }

        Calendar cal = Calendar.getInstance(zulu);
        cal.setTime(moment);

        picker = new CalendarPicker();
        picker.setCalendar(cal);
        picker.setShowTime(true);

    }

    @Override
    public void focus() {
        // TODO Auto-generated method stub

    }

    @Override
    public Node getNode() {
        return picker;
    }

    @Override
    public boolean isPopOver() {
        return true;
    }

    @Override
    protected boolean validate(String input) {
        // TODO Auto-generated method stub
        return true;
    }

}
