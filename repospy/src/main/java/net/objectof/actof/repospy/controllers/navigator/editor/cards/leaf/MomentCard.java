package net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;
import net.objectof.actof.widgets.KeyValuePane;
import net.objectof.model.impl.IMoment;


public class MomentCard extends LeafCard {

    Spinner<Integer> hour, minute, second;
    DatePicker picker = new DatePicker();
    Calendar cal = Calendar.getInstance();
    Button reset;

    KeyValuePane pane = new KeyValuePane();

    public MomentCard(ILeafNode entry) {
        super(entry);

        hour = new TimeSpinner(0, 23);
        minute = new TimeSpinner(0, 59);
        second = new TimeSpinner(0, 59);

        reset = new Button("Reset");
        reset.getStyleClass().add("tool-bar-button");
        reset.setPadding(new Insets(6));
        reset.setOnAction(event -> {
            getEntry().setFieldValue(new IMoment());
        });

        picker.setPromptText("Date");
        picker.setConverter(new StringConverter<LocalDate>() {

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(IMoment.DATE_FORMAT);

            @Override
            public String toString(LocalDate localDate) {
                return dateFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String string) {
                return fromMoment(new IMoment(string));
            }
        });

        HBox timebox = new HBox(3, hour, new Label(":"), minute, new Label(":"), second, reset);
        timebox.setAlignment(Pos.CENTER_LEFT);

        pane.setVgap(6);
        pane.setKeyVAlignment(VPos.CENTER);
        pane.put("Date", picker);
        pane.put("Time", timebox);

        picker.valueProperty().addListener(change -> updateModel());
        hour.valueProperty().addListener(change -> updateModel());
        minute.valueProperty().addListener(change -> updateModel());
        second.valueProperty().addListener(change -> updateModel());

        updateFromEntry();
        setContent(pane, false);

    }

    private void updateModel() {
        if (isUpdating()) { return; }
        getEntry().setFieldValue(buildMoment());
    }

    @Override
    public void updateUIFromEntry() {
        IMoment moment = (IMoment) getEntry().getFieldValue();
        if (moment == null) {
            picker.setDisable(true);
            hour.setDisable(true);
            minute.setDisable(true);
            second.setDisable(true);

            picker.setValue(fromMoment(new IMoment(0)));
            hour.getValueFactory().setValue(0);
            minute.getValueFactory().setValue(0);
            second.getValueFactory().setValue(0);

        } else {
            picker.setDisable(false);
            hour.setDisable(false);
            minute.setDisable(false);
            second.setDisable(false);

            cal.setTime(moment);
            picker.setValue(fromMoment(moment));
            hour.getValueFactory().setValue(cal.get(Calendar.HOUR_OF_DAY));
            minute.getValueFactory().setValue(cal.get(Calendar.MINUTE));
            second.getValueFactory().setValue(cal.get(Calendar.SECOND));

        }

    }

    private LocalDate fromMoment(IMoment moment) {
        if (moment == null) {
            moment = new IMoment();
        }
        cal.setTime(moment);
        return LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE));
    }

    private IMoment buildMoment() {

        cal.setTime(new Date());
        cal.set(Calendar.YEAR, picker.getValue().getYear());
        cal.set(Calendar.MONTH, picker.getValue().getMonthValue() - 1);
        cal.set(Calendar.DATE, picker.getValue().getDayOfMonth());
        cal.set(Calendar.HOUR_OF_DAY, hour.getValue());
        cal.set(Calendar.MINUTE, minute.getValue());
        cal.set(Calendar.SECOND, second.getValue());

        return new IMoment(cal.getTime().getTime());
    }
}

class TimeSpinner extends Spinner<Integer> {

    public TimeSpinner(int min, int max) {
        super(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max));
        setEditable(true);
        getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
        setPrefWidth(32);
        getEditor().setPadding(new Insets(2));
        getEditor().setAlignment(Pos.CENTER);

        format();
        getEditor().textProperty().addListener(change -> format());

        focusedProperty().addListener(change -> {
            if (!isFocused()) {
                try {

                    Integer value = Integer.parseInt(getEditor().getText());
                    getValueFactory().setValue(value);
                }
                catch (NumberFormatException e) {
                    getEditor().setText(getValue().toString());
                }
            }
        });
    }

    private void format() {
        if (getEditor().getText().length() == 1) {
            getEditor().setText("0" + getEditor().getText());
        }
    }

}
