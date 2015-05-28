package uk.ac.stfc.isis.ibex.ui;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public abstract class Waiting extends ModelObject {
	
	public abstract boolean isWaiting();
	
	public abstract WaitingInfo getDetails();
}
