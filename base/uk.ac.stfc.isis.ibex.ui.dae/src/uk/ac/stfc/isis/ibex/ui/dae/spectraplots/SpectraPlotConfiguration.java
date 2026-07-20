package uk.ac.stfc.isis.ibex.ui.dae.spectraplots;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.service.prefs.Preferences;

import uk.ac.stfc.isis.ibex.instrument.channels.DoubleChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;

/**
 * A configuration for a spectra plot.
 */
public class SpectraPlotConfiguration {

	private static final String TYPE_PREF_KEY = "type";
	private static final String PERIOD_PREF_KEY = "period";
	private static final String SPECTRUM_NUMBER_PREF_KEY = "spectrumNumber";

	private final int plotNumber;

	private final PersistedLocalChannel<Double> specNum;
	private final PersistedLocalChannel<Double> specPeriod;
	private final PersistedLocalChannel<String> specPeriodStr;
	private final PersistedLocalChannel<String> specMode;
	
	/**
	 * Constructor for the spectra plot configuration.
	 * @param plotNumber The plot number that this configuration is for.
	 */
    public SpectraPlotConfiguration(int plotNumber) {
    	this.plotNumber = plotNumber;
    	
    	final Preferences preferences = ConfigurationScope.INSTANCE
    			.getNode("uk.ac.stfc.isis.ibex.instrument")
    			.node("spectrumPreferences" + plotNumber);
    	
    	this.specNum = new PersistedLocalChannel<Double>(
    			getLocalPvName("SPECTRA"), 
    			new DoubleChannel(), 
    			i -> preferences.putDouble(SPECTRUM_NUMBER_PREF_KEY, i), 
    			() -> preferences.getDouble(SPECTRUM_NUMBER_PREF_KEY, 1.)
    	);
    	
    	this.specPeriod = new PersistedLocalChannel<Double>(
    			getLocalPvName("PERIOD"), 
    			new DoubleChannel(), 
    			i -> preferences.putDouble(PERIOD_PREF_KEY, i), 
    			() -> preferences.getDouble(PERIOD_PREF_KEY, 1.)
    	);
    	
        this.specPeriodStr = new PersistedLocalChannel<String>(
                getLocalPvName("PERIOD_STR"), 
                new StringChannel(), 
                i -> { }, 
                () -> String.valueOf(preferences.getDouble(PERIOD_PREF_KEY, 1.))
        );
    	
    	this.specMode = new PersistedLocalChannel<String>(
    			getLocalPvName("MODE"), 
    			new StringChannel(), 
    			i -> preferences.put(TYPE_PREF_KEY, i), 
    			() -> preferences.get(TYPE_PREF_KEY, "counts/us")
    	);

    }
    
    /**
     * Initialise the current values, and subscribe to future updates.
     */
    public void initializeFromPreferenceStore() {
    	specNum.setInitialValueAndSubscribeToChanges();
    	specPeriod.setInitialValueAndSubscribeToChanges();
    	specPeriodStr.setInitialValueAndSubscribeToChanges();
    	specMode.setInitialValueAndSubscribeToChanges();
    }
    
    /**
     * Gets the macros.
     *
     * @return the macros
     */
    public Map<String, String> getMacros() {
    	Map<String, String> res = new HashMap<>();
    	res.put("SPECTRUM_PV" + plotNumber, specNum.getPVAddress());
    	res.put("PERIOD_PV" + plotNumber, specPeriod.getPVAddress());
        res.put("PERIOD_PV_STR" + plotNumber, specPeriodStr.getPVAddress());
    	res.put("MODE_PV" + plotNumber, specMode.getPVAddress());
    	return res;
    }
    
    private String getLocalPvName(String name) {
    	return String.format("loc://_LOCAL:SPEC:%s%s", name, plotNumber);
    }
}
