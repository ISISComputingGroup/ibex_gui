package uk.ac.stfc.isis.ibex.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

/*
 * Performs an action once all updated values have a value
 */
public class UpdatedValues {

	private final Map<UpdatedValue<?>, Boolean> status = new HashMap<>();
	private SettableUpdatedValue<Boolean> allSet = new SettableUpdatedValue<>(false);
	
	private UpdatedValues(UpdatedValue<?> first, UpdatedValue<?>... remaining) {		
		add(first);
		for (UpdatedValue<?> other : remaining) {
			add(other);
		}
		
		update();
	}

	public static UpdatedValues awaitValues(UpdatedValue<?> first, UpdatedValue<?>... remaining) {
		return new UpdatedValues(first, remaining);
	}
	
	public boolean allSet() {
		return allSet.getValue();
	}

	public void onAllSet(Runnable action) {
		if (allSet.getValue()) {
			action.run();
			return;
		}
		
		allSet.addPropertyChangeListener(perform(action));
	}
	
	private PropertyChangeListener perform(final Runnable action) {
		return new PropertyChangeListener() {			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (allSet.getValue()) {
					action.run();
				}
			}
		};
	}
	
	private synchronized void update() {		
		allSet.setValue(areAllSet());
	}
	
	private void add(final UpdatedValue<?> value) {
		updateStatus(value);
		if (!status.get(value)) {
			PropertyChangeListener onValueSet = getListener(value);
			value.addPropertyChangeListener(onValueSet);
		}
	}

	private PropertyChangeListener getListener(final UpdatedValue<?> value) {
		PropertyChangeListener onValueSet = new PropertyChangeListener() {	
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				status.put(value, true);
				value.removePropertyChangeListener(this);
				update();
			}
		};
		
		return onValueSet;
	}

	private boolean areAllSet() {
		for (Boolean wasSet : status.values()) {
			if (!wasSet) {
				return false;
			}			
		}
		
		return true;
	}
	
	private void updateStatus(final UpdatedValue<?> value) {
		status.put(value, value.isSet());
	}
}
