package net.objectof.actof.repospy.controllers.navigator.composite.editors.primitive;


import java.util.Calendar;
import java.util.TimeZone;

import javafx.scene.Node;
import jfxtras.scene.control.CalendarPicker;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.AbstractEditor;
import net.objectof.actof.repospy.controllers.navigator.kind.LeafEntry;
import net.objectof.model.impl.IMoment;


public class MomentEditor extends AbstractEditor {

    private CalendarPicker picker;

    public MomentEditor(LeafEntry entry) {
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

        picker.calendarProperty().addListener(change -> {
            IMoment newMoment = new IMoment(picker.getCalendar().getTime().getTime(), zulu);
            getEntry().userInputValue(newMoment.toString(zulu));
        });

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
    protected boolean validate(String input) {
        return true;
    }

    public boolean expand() {
        return false;
    }

}
