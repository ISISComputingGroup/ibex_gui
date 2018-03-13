package uk.ac.stfc.isis.ibex.ui.logplotter;

import org.csstudio.trends.databrowser2.model.PVItem;
import org.eclipse.swt.widgets.Display;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.Observable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;

/**
 * An extension of the PVItem CSS class that has units associated with it.
 */
public class PVItemWithUnits extends PVItem {

	private String units;
	
	private static final String FIELD_SEPARATOR = ".";
	private static final String EGU_SUFFIX = FIELD_SEPARATOR + "EGU";
	private static final String VAL_SUFFIX = FIELD_SEPARATOR + "VAL";

	/**
	 * Constructor for a PV item with units. Attempts to figure out the location of the relevant EGU PV.
	 * 
	 * {@inheritDoc}
	 */
	public PVItemWithUnits(String pvAddress, double period) throws Exception {
		this(pvAddress, getEguPv(pvAddress), period);
	}
	
	/**
	 * Constructor for a PV item with units, with an explicit PV to use for the EGU.
	 * 
	 * {@inheritDoc}
	 */
	public PVItemWithUnits(String pvAddress, String eguPv, double period) throws Exception {
		super(pvAddress, period);
		
		if (!Strings.isNullOrEmpty(eguPv)) {
			final ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);
			final Observable<String> unitsObservable = obsFactory.getSwitchableObservable(new StringChannel(), eguPv);
			
			unitsObservable.addObserver(new BaseObserver<String>() {
				
				/**
				 * @{inheritDoc}
				 */
				@Override
				public void onValue(String value) {
					setUnits(value);
				}
				
				/**
				 * @{inheritDoc}
				 */
				@Override
				public void onError(Exception e) {
					setUnits(null);
				}
				
				/**
				 * @{inheritDoc}
				 */
				@Override
				public void onConnectionStatus(boolean isConnected) {
					if (!isConnected) {
						setUnits(null);
					}
				}
			});
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * Overrides the default implementation of getDisplayName to append the units in brackets (if non-null).
	 * 
	 * If the units are null, falls back to the default implementation.
	 */
	@Override
	public synchronized String getDisplayName() {		
		if (Strings.isNullOrEmpty(getUnits())) {
			return super.getDisplayName();
		} else {
			return String.format("%s (%s)", super.getDisplayName(), getUnits());
		}
	}
	
	/**
	 * Sets the units and fires a property change on the GUI thread to let CSS know something has changed.
	 * @param units the new units to set.
	 */
	private synchronized void setUnits(String units) {
		this.units = units;
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				fireItemLookChanged();
			}
		});
	}
	
	/**
	 * Gets the units of this PV.
	 * @return the units.
	 */
	private String getUnits() {
		return units;
	}
	
	/**
	 * Gets the corresponding EGU PV for a given PV address.
	 * @param pvAddress the pv address to get the EGU of.
	 * @return the PV that contains the EGU.
	 */
	public static String getEguPv(String pvAddress) {
		
		// If the PV ends in .VAL, strip off the .VAL
		if (pvAddress.endsWith(VAL_SUFFIX)) {
			pvAddress = pvAddress.substring(0, pvAddress.length() - VAL_SUFFIX.length());
		}
		
		// If the PV is still a field, we don't have a way to get the EGU for it, so just return null here.
		if (pvAddress.contains(FIELD_SEPARATOR)) {
			return null;
		}
		
		return pvAddress + EGU_SUFFIX;
	}

}
