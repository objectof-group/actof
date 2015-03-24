package net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf;


import java.util.Calendar;
import java.util.TimeZone;

import jfxtras.scene.control.CalendarPicker;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;
import net.objectof.model.impl.IMoment;


public class MomentCard extends LeafCard {

    CalendarPicker picker = new CalendarPicker();
    TimeZone zulu = TimeZone.getTimeZone("GMT");

    public MomentCard(ILeafNode entry) {
        super(entry);

        picker.setAllowNull(false);
        picker.setShowTime(true);
        picker.calendarProperty().addListener(change -> {
            if (isUpdating()) { return; }
            IMoment newMoment = new IMoment(picker.getCalendar().getTime().getTime());
            getEntry().setFieldValue(newMoment);
        });

        updateFromEntry();
        setContent(picker, false);

    }

    @Override
    public void updateUIFromEntry() {

        IMoment moment = (IMoment) getEntry().getFieldValue();
        if (moment == null) {
            moment = new IMoment();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(moment);
        picker.setCalendar(cal);

    }

}
