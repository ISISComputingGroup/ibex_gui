package uk.ac.stfc.isis.ibex.ui.dae;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.dae.Dae;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.Waiting;

/**
 * Class that observes DAE PVs and tells the GUI to wait if the DAE is currently changing settings.
 */
public class WaitForDaeChanges extends Waiting {
	
    private final UpdatedValue<Boolean> inSettingsChange;
	
	private boolean isWaiting;
	
	private static final Logger LOG = IsisLog.getLogger(WaitForDaeChanges.class);
	
	/**
	 * Class that observes DAE PVs and tells the GUI to wait if the DAE is currently changing settings.
	 */
	public WaitForDaeChanges() {
		inSettingsChange = new UpdatedObservableAdapter<>(Dae.getInstance().observables().currentlyChangingSettings);
		
		inSettingsChange.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Boolean isInTransition = inSettingsChange.getValue();
				if (isInTransition != null) {
					setIsWaiting(isInTransition);
				}
			}
		}, true);
	}

	/**
	 * Returns whether the GUI should be waiting for DAE changes to complete.
	 * @return whether the GUI should be waiting for DAE changes to complete
	 */
	@Override
	public boolean isWaiting() {
		return isWaiting;
	}
	
	private void setIsWaiting(Boolean status) {
		LOG.info(status ? "Waiting for DAE changes to complete" : "Finished waiting for DAE changes to complete");
		firePropertyChange("isWaiting", this.isWaiting, this.isWaiting = status);
	}
}
