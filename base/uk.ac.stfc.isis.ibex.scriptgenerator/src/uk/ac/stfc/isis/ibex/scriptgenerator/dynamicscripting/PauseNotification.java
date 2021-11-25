package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import uk.ac.stfc.isis.ibex.model.HasStatus;

/**
 * A notification that the script paused status may have been updated. The
 * different states have no purpose other than to force a property change.
 */
public enum PauseNotification implements HasStatus<PauseNotification> {
	
	/**
	 * Dummy enum for the old state.
	 */
	OLD_STATE,
	
	/**
	 * Dummy enum for the new state.
	 */
	NEW_STATE;
	
	/**
	 * @return this status.
	 */
	public PauseNotification getStatus() {
		return this;
	}

}
