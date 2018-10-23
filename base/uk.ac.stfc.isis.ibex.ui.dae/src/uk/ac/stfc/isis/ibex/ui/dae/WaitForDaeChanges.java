package uk.ac.stfc.isis.ibex.ui.dae;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import uk.ac.stfc.isis.ibex.dae.Dae;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.Waiting;

public class WaitForDaeChanges extends Waiting {
	
    private final UpdatedValue<Boolean> inTransition;
	
	private boolean isWaiting;
	
	public WaitForDaeChanges() {
		inTransition = new UpdatedObservableAdapter<>(Dae.getInstance().observables().inStateTransition);
		
		inTransition.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Boolean isInTransition = inTransition.getValue();
				if (isInTransition != null) {
					setIsWaiting(isInTransition);
				}
			}
		}, true);
	}

	@Override
	public boolean isWaiting() {
		return isWaiting;
	}
	
	private void setIsWaiting(Boolean status) {
		firePropertyChange("isWaiting", this.isWaiting, this.isWaiting = status);
	}
}
