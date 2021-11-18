package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import uk.ac.stfc.isis.ibex.model.HasStatus;

/**
 * Status of the dynamic scripting.
 */
public enum DynamicScriptingStatus implements HasStatus<DynamicScriptingStatus> {
	
	/**
	 * Dynamic scripting is currently playing
	 */
	PLAYING,
	/**
	 * Dynamic scripting is currently stopped.
	 */
	STOPPED,
	/**
	 * Dynamic scripting is in error.
	 */
	ERROR,
	/**
	 * Dynamic scripting is currently paused.
	 */
	PAUSED;

	@Override
	public DynamicScriptingStatus getStatus() {
		return this;
	}

}
