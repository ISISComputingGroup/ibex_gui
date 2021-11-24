package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import uk.ac.stfc.isis.ibex.model.HasStatus;

public enum PauseNotification implements HasStatus<PauseNotification>{
	
	OLD_STATE,
	NEW_STATE;
	
	public PauseNotification getStatus() {
		return this;
	}

}
