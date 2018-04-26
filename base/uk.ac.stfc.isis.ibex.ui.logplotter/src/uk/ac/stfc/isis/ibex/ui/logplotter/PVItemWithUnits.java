package uk.ac.stfc.isis.ibex.ui.logplotter;

import org.csstudio.trends.databrowser2.model.AxisConfig;
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

	private String displayNameWithoutUnits;
	
	private AxisConfig axis;
	
	private static final String FIELD_SEPARATOR = ".";
	private static final String EGU_SUFFIX = FIELD_SEPARATOR + "EGU";
	private static final String VAL_SUFFIX = FIELD_SEPARATOR + "VAL";

	/**
	 * Constructor for a PV item with units. Attempts to figure out the location of the relevant EGU PV.
	 * 
	 * @param displayName the user friendly name of the PV
	 * @param pvAddress the address of the PV to plot
	 * @param period the scan period in seconds
	 * @param axis the axis that the PV is plotted on
	 * @throws Exception on error
	 */
	public PVItemWithUnits(String displayName, String pvAddress, double period, AxisConfig axis) throws Exception {
		this(displayName, pvAddress, getEguPv(pvAddress), period, axis);
	}
	
	/**
	 * Constructor for a PV item with units, with an explicit PV to use for the EGU.
	 * 
	 * @param displayName the user friendly name of the PV
	 * @param pvAddress the address of the PV to plot
	 * @param eguPv the PV which contains the EGU
	 * @param period the scan period in seconds
	 * @param axis the axis that the PV is plotted on
	 * @throws Exception on error
	 */
	public PVItemWithUnits(String displayName, String pvAddress, String eguPv, double period, AxisConfig axis) throws Exception {
		super(pvAddress, period);
		this.displayNameWithoutUnits = displayName;
		this.axis = axis;
		
		if (!Strings.isNullOrEmpty(eguPv)) {
			final ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);
			final Observable<String> unitsObservable = obsFactory.getSwitchableObservable(new StringChannel(), eguPv);
			
			unitsObservable.addObserver(new BaseObserver<String>() {
				
				/**
				 * @{inheritDoc}
				 */
				@Override
				public void onValue(String value) {
					setPVUnits(value);
				}
				
				/**
				 * @{inheritDoc}
				 */
				@Override
				public void onError(Exception e) {
					setPVUnits(null);
				}
				
				/**
				 * @{inheritDoc}
				 */
				@Override
				public void onConnectionStatus(boolean isConnected) {
					if (!isConnected) {
						setPVUnits(null);
					}
				}
			});
		}
	}
	
	/**
	 * Sets the units and fires a property change on the GUI thread to let CSS know something has changed.
	 * @param units the new units to set.
	 */
	private synchronized void setPVUnits(String units) {
		final String displayString = Strings.isNullOrEmpty(units) ? displayNameWithoutUnits : String.format("%s (%s)", displayNameWithoutUnits, units);
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				setDisplayName(displayString);
				fireItemLookChanged();
				axis.setName(displayString);
			}
		});
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
