package uk.ac.stfc.isis.ibex.ui.banner.models;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.instrument.status.InstrumentStatusVariables;
import uk.ac.stfc.isis.ibex.model.ModelObject;

public class InstrumentStatusViewModel extends ModelObject {

	private boolean runControlRunning;
	
	private final BaseObserver<Boolean> runControlStatusObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionStatus(boolean isConnected) {
			setRunControlRunning(isConnected);
		}
	};
	
	public void setRunControlRunning(boolean isRunning) {
        firePropertyChange("runControlRunning", this.runControlRunning, this.runControlRunning = isRunning);
	}
	
	public boolean getRunControlRunning() {
		return runControlRunning;
	}
	
	public InstrumentStatusViewModel(InstrumentStatusVariables variables) {
		variables.runcontrolPV.subscribe(runControlStatusObserver);
	}
}
