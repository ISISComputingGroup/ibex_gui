package uk.ac.stfc.isis.ibex.ui.configserver;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.ServerStatus;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.Waiting;
import uk.ac.stfc.isis.ibex.ui.WaitingInfo;

public class WaitForServer extends Waiting {

	private final UpdatedValue<ServerStatus> serverStatus;
	
	private boolean isWaiting;
	private WaitingInfo details = new WaitingInfo("");
	
	public WaitForServer() {
		serverStatus = new UpdatedObservableAdapter<>(Configurations.getInstance().server().serverStatus());
		
		serverStatus.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ServerStatus status = serverStatus.getValue();
				if (status != null) {
					setIsWaiting(status);
					setDetails(status);
				}
			}
		}, true);
	}

	@Override
	public boolean isWaiting() {
		return isWaiting;
	}

	@Override
	public WaitingInfo getDetails() {
		return details;
	}
	
	private void setIsWaiting(ServerStatus status) {
		firePropertyChange("isWaiting", this.isWaiting, this.isWaiting = status.isBusy());
	}
	
	private void setDetails(ServerStatus status) {		
		firePropertyChange("details", this.details, this.details = new WaitingInfo(composeReason(status)));
	}

	private String composeReason(ServerStatus status) {
		return status.isBusy() ? "Waiting" : "Ready";
	}
}
