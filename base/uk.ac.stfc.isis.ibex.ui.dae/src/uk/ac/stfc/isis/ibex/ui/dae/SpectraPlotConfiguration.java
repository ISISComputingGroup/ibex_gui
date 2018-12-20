package uk.ac.stfc.isis.ibex.ui.dae;

import com.google.common.collect.Maps;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.service.prefs.Preferences;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.Observable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.channels.DoubleChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

public class SpectraPlotConfiguration {
	
	private static final Logger LOG = IsisLog.getLogger(SpectraPlotConfiguration.class);

	private final Preferences preferences;

	private final String specNumPvName;
	private final String specPeriodPvName;
	private final String specModePvName;
	
	private static final WritableFactory WRITE_FACTORY = new WritableFactory(OnInstrumentSwitch.NOTHING);
	private static final ObservableFactory OBS_FACTORY = new ObservableFactory(OnInstrumentSwitch.NOTHING);

	private final int plotNumber;

	private final Writable<Double> specNumWritable;
	private final Writable<Double> specPeriodWritable;
	private final Writable<String> specModeWritable;
	private final Observable<Double> specNumObserver;
	private final Observable<Double> specPeriodObserver;
	private final Observable<String> specModeObserver;
	
    public SpectraPlotConfiguration(int plotNumber) {
    	this.specNumPvName = makeLocalPvName("SPECTRA", plotNumber);
    	this.specPeriodPvName = makeLocalPvName("PERIOD", plotNumber);
    	this.specModePvName = makeLocalPvName("MODE", plotNumber);
    	
    	this.specNumWritable = WRITE_FACTORY.getSwitchableWritable(new DoubleChannel(), specNumPvName);
    	this.specPeriodWritable = WRITE_FACTORY.getSwitchableWritable(new DoubleChannel(), specPeriodPvName);
    	this.specModeWritable = WRITE_FACTORY.getSwitchableWritable(new StringChannel(), specModePvName);
    	
    	this.specNumObserver = OBS_FACTORY.getSwitchableObservable(new DoubleChannel(), specNumPvName);
    	this.specPeriodObserver = OBS_FACTORY.getSwitchableObservable(new DoubleChannel(), specPeriodPvName);
    	this.specModeObserver = OBS_FACTORY.getSwitchableObservable(new StringChannel(), specModePvName);
    	
    	this.plotNumber = plotNumber;
    	
    	preferences = ConfigurationScope.INSTANCE.getNode("uk.ac.stfc.isis.ibex.instrument").node("spectrumPreferences" + plotNumber);
    }
    
    public void initializeFromPreferenceStore() {
    	try {
	    	specNumWritable.write(preferences.getDouble("spectrumNumber", 1.0));
    	} catch (Throwable e) {
    		LoggerUtils.logErrorWithStackTrace(LOG, e.getMessage(), e);
    	}
    	try {
	    	specPeriodWritable.write(preferences.getDouble("period", 1.0));
    	} catch (Throwable e) {
    		LoggerUtils.logErrorWithStackTrace(LOG, e.getMessage(), e);
    	}
    	try {
	    	specModeWritable.write(preferences.get("type", "counts/us"));
    	} catch (Throwable e) {
    		LoggerUtils.logErrorWithStackTrace(LOG, e.getMessage(), e);
    	}
    	
    	addChangeListeners();
    }
    
    private void addChangeListeners() {
    	specNumObserver.addObserver(new BaseObserver<Double>() {
    		@Override
    		public void onValue(Double value) {
    			preferences.putDouble("spectrumNumber", value);
    		}
		});
    	
    	specPeriodObserver.addObserver(new BaseObserver<Double>() {
    		@Override
    		public void onValue(Double value) {
    			preferences.putDouble("period", value);
    		}
		});
    	
    	specModeObserver.addObserver(new BaseObserver<String>() {
    		@Override
    		public void onValue(String value) {
    			preferences.put("type", value);
    		}
		});
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
