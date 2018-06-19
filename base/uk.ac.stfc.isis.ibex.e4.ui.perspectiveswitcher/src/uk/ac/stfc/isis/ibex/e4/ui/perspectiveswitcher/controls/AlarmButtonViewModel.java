package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.alarm.AlarmCounter;

/**
 * A model to provide easy access to the listeners for the interaction with the
 * alarm system; chiefly the number of active alarms.
 */
public final class AlarmButtonViewModel extends PerspectiveButtonViewModel {

    private static final String ALARM = "Alarms";
    private static final Color ALARM_COLOR = SWTResourceManager.getColor(250, 150, 150);
    private static final int MAX_ALARMS = 100;

    private int alarmCount = 0;

    /**
     * Instantiates a new alarm count view model.
     *
     * @param alarmCounter
     *            the alarm counter model to listen to
     * @param buttonLabel
     *            the label to appear on the button
     */
    public AlarmButtonViewModel(final AlarmCounter alarmCounter, String buttonLabel) {
        super(buttonLabel);
        alarmCounter.addPropertyChangeListener("alarmCount", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent arg0) {
                alarmCount = alarmCounter.getCount();
                update();
            }
        });
        update();

    }

    /**
     * Updates the model with the new alarm count.
     *
     * @param count
     *            the current alarm count
     */
    private void update() {
        setText(ALARM + alarmCountAsText());
        setColor(chooseColor());
    }

    private String alarmCountAsText() {
        String countText;
        if (alarmCount > 0) {
            countText = " ("
                    + (alarmCount > MAX_ALARMS ? Integer.toString(MAX_ALARMS) + "+" : Integer.toString(alarmCount))
                    + ")";
        } else {
            countText = "";
        }
        return countText;
    }

    private Color chooseColor() {
        Color color;
        if (alarmCount > 0) {
            color = ALARM_COLOR;
        } else if (active) {
            color = ACTIVE;
        } else if (inFocus) {
            color = FOCUSSED;
        } else {
            color = DEFOCUSSED;
        }
        return color;
    }

    @Override
    public void setFocus(boolean inFocus) {
        super.setFocus(inFocus);
        update();
    }
}
