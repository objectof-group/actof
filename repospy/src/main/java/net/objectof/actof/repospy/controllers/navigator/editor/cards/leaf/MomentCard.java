package net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf;


import java.util.Calendar;
import java.util.TimeZone;

import jfxtras.scene.control.CalendarPicker;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;
import net.objectof.model.impl.IMoment;


public class MomentCard extends LeafCard {

    public MomentCard(ILeafNode entry, boolean capitalize) {
        super(entry, capitalize);

        TimeZone zulu = TimeZone.getTimeZone("GMT");
        IMoment moment = (IMoment) entry.getFieldValue();
        if (moment == null) {
            moment = new IMoment();
        }

        Calendar cal = Calendar.getInstance(zulu);
        cal.setTime(moment);

        CalendarPicker picker = new CalendarPicker();
        picker.setCalendar(cal);
        picker.setShowTime(true);

        picker.calendarProperty().addListener(change -> {
            IMoment newMoment = new IMoment(picker.getCalendar().getTime().getTime(), zulu);
            getEntry().userInputString(newMoment.toString(zulu));
        });

        setContent(picker, false);

    }

}
