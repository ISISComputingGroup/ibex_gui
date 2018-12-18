package uk.ac.stfc.isis.ibex.ui.dae;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Map;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.service.prefs.Preferences;

import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.instrument.channels.DoubleChannel;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

public class SpectraPlotConfiguration {
	
	private final Preferences preferences;

	private String specNumPvName;
	private String specPeriodPvName;
	private String specModePvName;
	
	private final WritableFactory writeFactory = new WritableFactory(OnInstrumentSwitch.NOTHING);

	private int plotNumber;
	
    public SpectraPlotConfiguration(int plotNumber) {
    	this.specNumPvName = makeLocalPvName("SPECTRA", plotNumber);
    	this.specPeriodPvName = makeLocalPvName("PERIOD", plotNumber);
    	this.specModePvName = makeLocalPvName("MODE", plotNumber);
    	this.plotNumber = plotNumber;
    	
    	preferences = ConfigurationScope.INSTANCE.getNode("uk.ac.stfc.isis.ibex.instrument").node("spectrumPreferences" + plotNumber);
    }
    
    public void initializeFromPreferenceStore() {
    	try {
	    	writeFactory.getSwitchableWritable(new DoubleChannel(), specNumPvName).write(preferences.getDouble("spectrumNumber", 1.0));
    	} catch (IOException e) {
    		LoggerUtils.logErrorWithStackTrace(IsisLog.getLogger(getClass()), e.getMessage(), e);
    	}
    	try {
	    	writeFactory.getSwitchableWritable(new DoubleChannel(), specPeriodPvName).write(preferences.getDouble("period", 1.0));
    	} catch (IOException e) {
    		LoggerUtils.logErrorWithStackTrace(IsisLog.getLogger(getClass()), e.getMessage(), e);
    	}
    	try {
	    	writeFactory.getSwitchableWritable(new DoubleChannel(), specModePvName).write(preferences.getDouble("typeSelectionIndex", 0.0));
    	} catch (IOException e) {
    		LoggerUtils.logErrorWithStackTrace(IsisLog.getLogger(getClass()), e.getMessage(), e);
    	}
    }
    
    public Map.Entry<String, String> getSpecNumMacro() {
    	return Maps.immutableEntry("SPECTRUM_PV" + plotNumber, specNumPvName);
    }
    
    public Map.Entry<String, String> getSpecPeriodsMacro() {
    	return Maps.immutableEntry("PERIOD_PV" + plotNumber, specPeriodPvName);
    }
    
    public Map.Entry<String, String> getSpecModeMacro() {
    	return Maps.immutableEntry("MODE_PV" + plotNumber, specModePvName);
    }
    
    private static String makeLocalPvName(String name, int number) {
    	return String.format("loc://_LOCAL:SPEC:%s%s", name, number);
    }
}
