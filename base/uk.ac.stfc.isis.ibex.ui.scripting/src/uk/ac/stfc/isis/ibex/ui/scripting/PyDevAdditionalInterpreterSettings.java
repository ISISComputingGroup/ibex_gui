package uk.ac.stfc.isis.ibex.ui.scripting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.python.pydev.ui.pythonpathconf.InterpreterNewCustomEntriesAdapter;

import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;

public class PyDevAdditionalInterpreterSettings extends InterpreterNewCustomEntriesAdapter {
		
	@Override
	public Collection<String> getAdditionalEnvVariables() {
		List<String> entriesToAdd = new ArrayList<String>();
		
		entriesToAdd.add(pvPrefix());
		entriesToAdd.add(epicsBasePath());
		addEpicsEnvironment(entriesToAdd);

		return entriesToAdd;
	}
	
	@Override
	public Collection<String> getAdditionalLibraries() {
		List<String> entriesToAdd = new ArrayList<String>();
				
		entriesToAdd.add(geniePythonPath());
		entriesToAdd.add(pyEpicsPath());
		
		return entriesToAdd;
	}
	
	private String pvPrefix() {
		return "MYPVPREFIX=" + Instrument.getInstance().currentInstrument().pvPrefix();
	}
	
	private String epicsBasePath() {
		return "PATH=" + toOSPath(PreferenceSupplier.epicsBase())  + ";" + toOSPath(PreferenceSupplier.epicsUtilsPath()) + ";${PATH}";
	}
	
	private String pyEpicsPath() {
		return toOSPath(PreferenceSupplier.pyEpicsPath());
	}

	private String geniePythonPath() {
		return toOSPath(PreferenceSupplier.geniePythonPath());
	}
	
	private void addEpicsEnvironment(List<String> entries) {
		for (String entry : epicsEnvironment()) {
			entries.add(entry);
		}
	}
	
	// Get the channel access environment variables from EPICS preferences
	private List<String> epicsEnvironment() {
		String id = "org.csstudio.platform.libs.epics";		
        final IPreferencesService prefs = Platform.getPreferencesService();
		List<String> epicsEnv = new ArrayList<String>();

        final String addList = prefs.getString(id, "addr_list", null, null);
        epicsEnv.add("EPICS_CA_ADDR_LIST=" + addList);
        
        final String autoAddr = Boolean.toString(
                prefs.getBoolean(id, "auto_addr_list", true, null));
        epicsEnv.add("EPICS_CA_AUTO_ADDR_LIST=" + autoAddr);
        
        final String maxArrayBytes =
                prefs.getString(id, "max_array_bytes", "16384", null);
        epicsEnv.add("EPICS_CA_MAX_ARRAY_BYTES=" + maxArrayBytes);
        
        return epicsEnv;
	}
	
	/**
	 * @param path the file path
	 * @return platform dependent path.
	 */
	private String toOSPath(String path) {
		return new Path(path).toOSString();
	}
}
