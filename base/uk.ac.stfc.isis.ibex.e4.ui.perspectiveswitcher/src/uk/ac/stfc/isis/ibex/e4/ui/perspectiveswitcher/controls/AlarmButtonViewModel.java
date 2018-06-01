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
	
	private String text = ALARM;
	
    /**
     * Instantiates a new alarm count view model.
     *
     * @param alarmCounter the alarm counter model to listen to
     */
	public AlarmButtonViewModel(final AlarmCounter alarmCounter) {
		super();
        alarmCounter.addPropertyChangeListener("alarmCount", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				update(alarmCounter.getCount());
				
			}
		});		
        update(alarmCounter.getCount());
    }
	
	public String getText() {
		return text;
	}
	
	private void setText(String newText) {
		firePropertyChange("text", text, text = newText);
	}
	
    /**
     * Updates the model with the new alarm count.
     *
     * @param count the current alarm count
     */
	private void update(long count) {
        setText(ALARM + alarmCountAsText(count));
        setColor(chooseColor(count));
	}
	
    private String alarmCountAsText(Long count) {
    	
    	String countText;
    	if (count > 0) {
    		countText = " ( " + (count > MAX_ALARMS ? Integer.toString(MAX_ALARMS) + "+" : count.toString()) + ")";
    	}
    	else {
    		countText = "";
    	}
    	return countText;
	}
    
    private Color chooseColor(Long alarmCount) {
    	Color color;
    	if (alarmCount > 0) {
    		color = ALARM_COLOR;
    	}
    	else if (active) {
    		color = ACTIVE;
    	}
    	else if (inFocus) {
    		color = FOCUSSED;
    	}
    	else {
    		color = DEFOCUSSED;
    	}
    	return color;
    }
}
