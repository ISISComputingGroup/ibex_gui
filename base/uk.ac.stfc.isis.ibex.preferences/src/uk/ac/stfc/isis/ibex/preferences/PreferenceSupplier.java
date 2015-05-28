package uk.ac.stfc.isis.ibex.preferences;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;

public class PreferenceSupplier extends AbstractPreferenceInitializer {
	
    public static final String EPICS_BASE_DIRECTORY = "epics_base_directory";
    public static final String Default_EPICS_BASE_DIRECTORY = "c:\\Instrument\\Apps\\EPICS\\base\\3-14-12-2\\bin\\windows-x64";

    public static final String PYTHON_INTERPRETER_PATH = "python_interpreter_path";
    public static final String Default_PYTHON_INTERPRETER_PATH = "C:\\Instrument\\Apps\\Python\\python.exe";
    
    public static final String GENIE_PYTHON_DIRECTORY = "genie_python_directory";
    public static final String Default_GENIE_PYTHON_DIRECTORY = "C:\\Instrument\\Apps\\Python\\Lib\\site-packages\\genie_python";

    public static final String PYEPICS_DIRECTORY = "pyepics_directory";
    public static final String Default_PYEPICS_DIRECTORY = "C:\\Instrument\\Apps\\Python\\Lib\\site-packages\\epics";

    public static final String INSTRUMENT_SCIENTIST_PASSWORD = "instrument_scientist_password";
    public static final String Default_INSTRUMENT_SCIENTIST_PASSWORD = "reliablebeam";
    
    public static final String ADMINISTRATOR_PASSWORD = "administrator_password";
    public static final String Default_ADMINISTRATOR_PASSWORD = "asyndriver";  

    public static final String INITIAL_USER = "initial_user";
    public static final String Default_INITIAL_USER = "Default user";
        
    
	public static final IScopeContext SCOPE_CONTEXT = InstanceScope.INSTANCE;
    public static final String PREFERENCE_NODE = "uk.ac.stfc.isis.ibex.preferences";
	
	public static IEclipsePreferences getPreferences() {
        return SCOPE_CONTEXT.getNode(PREFERENCE_NODE);
    }
		
	public static String epicsBase() {
		return getPreferences().get(EPICS_BASE_DIRECTORY, Default_EPICS_BASE_DIRECTORY);
	}
	
	public static String pythonInterpreterPath() {
		return getPreferences().get(PYTHON_INTERPRETER_PATH, Default_PYTHON_INTERPRETER_PATH);
	}
	
	public static String geniePythonPath() {
		return getPreferences().get(GENIE_PYTHON_DIRECTORY, Default_GENIE_PYTHON_DIRECTORY);
	}

	public static String pyEpicsPath() {
		return getPreferences().get(PYEPICS_DIRECTORY, Default_PYEPICS_DIRECTORY);
	}

	public static String administratorPassword() {
		return getPreferences().get(ADMINISTRATOR_PASSWORD, Default_ADMINISTRATOR_PASSWORD);
	}
	
	public static String instrumentScientistPassword() {
		return getPreferences().get(INSTRUMENT_SCIENTIST_PASSWORD, Default_INSTRUMENT_SCIENTIST_PASSWORD);
	}
	
	public static String initialUser() {
		return getPreferences().get(INITIAL_USER, Default_INITIAL_USER);
	}
	
	@Override
	public void initializeDefaultPreferences() {
        IPreferenceStore store = Preferences.getDefault().getPreferenceStore();
        Map<String, String> initializationEntries = PreferenceSupplier.getInitializationEntries();
        for (Map.Entry<String, String> entry : initializationEntries.entrySet()) {
            store.setDefault(entry.getKey(), entry.getValue());
        }
    }

	public static Map<String, String> getInitializationEntries() {
        Map<String, String> entries = new HashMap<String, String>();
        entries.put(EPICS_BASE_DIRECTORY, Default_EPICS_BASE_DIRECTORY);
        entries.put(PYTHON_INTERPRETER_PATH, Default_PYTHON_INTERPRETER_PATH);
        entries.put(GENIE_PYTHON_DIRECTORY,  Default_GENIE_PYTHON_DIRECTORY);
        entries.put(PYEPICS_DIRECTORY, Default_PYEPICS_DIRECTORY);
        entries.put(ADMINISTRATOR_PASSWORD, Default_ADMINISTRATOR_PASSWORD);
        entries.put(INITIAL_USER, Default_INITIAL_USER);
        
        return entries;
	}
}
