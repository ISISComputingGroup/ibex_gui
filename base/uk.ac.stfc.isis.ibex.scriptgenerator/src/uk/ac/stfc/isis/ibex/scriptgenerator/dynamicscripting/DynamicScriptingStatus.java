package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import uk.ac.stfc.isis.ibex.model.HasStatus;

public enum DynamicScriptingStatus implements HasStatus<DynamicScriptingStatus> {
	
	PLAYING,
	STOPPED,
	ERROR,
	PAUSED;

	@Override
	public DynamicScriptingStatus getStatus() {
		return this;
	}

}
