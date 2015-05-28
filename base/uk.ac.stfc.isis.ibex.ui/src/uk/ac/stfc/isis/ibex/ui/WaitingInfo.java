package uk.ac.stfc.isis.ibex.ui;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class WaitingInfo extends ModelObject {

	private String reason;

	public WaitingInfo(String reason) {
		this.reason = reason;
	}
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		firePropertyChange("reason", this.reason, this.reason = reason);
	}
}
