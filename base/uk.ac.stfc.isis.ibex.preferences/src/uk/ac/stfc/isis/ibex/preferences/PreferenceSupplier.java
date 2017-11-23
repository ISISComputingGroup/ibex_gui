
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
*
* You should have received a copy of the Eclipse Public License v1.0
* along with this program; if not, you can obtain a copy from
* https://www.eclipse.org/org/documents/epl-v10.php or 
* http://opensource.org/licenses/eclipse-1.0.php
*/

package uk.ac.stfc.isis.ibex.preferences;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Supplies the details for the IBEX preference page.
 */
public class PreferenceSupplier extends AbstractPreferenceInitializer {
	
    /**
     * The preference setting for the location of EPICS base.
     */
    public static final String EPICS_BASE_DIRECTORY = "epics_base_directory";

    /**
     * The default for the location of EPICS base.
     */
    public static final String DEFAULT_EPICS_BASE_DIRECTORY = "c:\\Instrument\\Apps\\EPICS\\base\\master\\bin\\windows-x64";

    /**
     * The preference setting for the location of Python.
     */
    public static final String PYTHON_INTERPRETER_PATH = "python_interpreter_path";

    /**
     * The default for the location of Python.
     */
    public static final String DEFAULT_PYTHON_INTERPRETER_PATH = "C:\\Instrument\\Apps\\Python\\python.exe";
    
    /**
     * The preference setting for the location of genie_python.
     */
    public static final String GENIE_PYTHON_DIRECTORY = "genie_python_directory";

    /**
     * The default for the location of genie_python.
     */
    public static final String DEFAULT_GENIE_PYTHON_DIRECTORY = "C:\\Instrument\\Apps\\Python\\Lib\\site-packages\\genie_python";
    
    /**
     * The preference setting for the location of EPICS utils.
     */
    public static final String EPICS_UTILS_DIRECTORY = "epics_utils_directory";

    /**
     * The default for the location of EPICS utils.
     */
    public static final String DEFAULT_EPICS_UTILS_DIRECTORY = "C:\\Instrument\\Apps\\EPICS_UTILS";

    /**
     * The preference setting for the instrument scientist password.
     */
    public static final String INSTRUMENT_SCIENTIST_PASSWORD = "instrument_scientist_password";

    /**
     * The default instrument scientist password.
     */
    public static final String DEFAULT_INSTRUMENT_SCIENTIST_PASSWORD = "reliablebeam";
    
    /**
     * The preference setting for the administrator password.
     */
    public static final String ADMINISTRATOR_PASSWORD = "administrator_password";

    /**
     * The default administrator password.
     */
    public static final String DEFAULT_ADMINISTRATOR_PASSWORD = "asyndriver";  

    /**
     * The preference setting for the initial user.
     */
    public static final String INITIAL_USER = "initial_user";

    /**
     * The default initial user.
     */
    public static final String DEFAULT_INITIAL_USER = "Default user";
    
    /**
     * The scope for the preference settings.
     */
	public static final IScopeContext SCOPE_CONTEXT = InstanceScope.INSTANCE;

    /**
     * The overall preferences name.
     */
    public static final String PREFERENCE_NODE = "uk.ac.stfc.isis.ibex.preferences";
	
    /**
     * Gets the IBEX preferences.
     * 
     * @return the preferences
     */
	public static IEclipsePreferences getPreferences() {
        return SCOPE_CONTEXT.getNode(PREFERENCE_NODE);
    }
		
    /**
     * Gets the preference setting for EPICS base directory.
     * 
     * @return the setting (uses default if not set)
     */
	public static String epicsBase() {
		return getPreferences().get(EPICS_BASE_DIRECTORY, DEFAULT_EPICS_BASE_DIRECTORY);
	}
	
    /**
     * Gets the preference setting for Python directory.
     * 
     * @return the setting (uses default if not set)
     */
	public static String pythonInterpreterPath() {
		return getPreferences().get(PYTHON_INTERPRETER_PATH, DEFAULT_PYTHON_INTERPRETER_PATH);
	}
	
    /**
     * Gets the preference setting for genie_python directory.
     * 
     * @return the setting (uses default if not set)
     */
	public static String geniePythonPath() {
		return getPreferences().get(GENIE_PYTHON_DIRECTORY, DEFAULT_GENIE_PYTHON_DIRECTORY);
	}
	
	/**
     * Gets the preference for the EPICS_UTIL directory.
     * 
     * @return the setting (uses default if not set)
     */
	public static String epicsUtilsPath() {
		return getPreferences().get(EPICS_UTILS_DIRECTORY, DEFAULT_EPICS_UTILS_DIRECTORY);
	}

    /**
     * Gets the preference for the administrator password.
     * 
     * @return the setting (uses default if not set)
     */
	public static String administratorPassword() {
		return getPreferences().get(ADMINISTRATOR_PASSWORD, DEFAULT_ADMINISTRATOR_PASSWORD);
	}
	
    /**
     * Gets the preference for the instrument password.
     * 
     * @return the setting (uses default if not set)
     */
	public static String instrumentScientistPassword() {
		return getPreferences().get(INSTRUMENT_SCIENTIST_PASSWORD, DEFAULT_INSTRUMENT_SCIENTIST_PASSWORD);
	}
	
    /**
     * Gets the preference for the initial user.
     * 
     * @return the setting (uses default if not set)
     */
	public static String initialUser() {
		return getPreferences().get(INITIAL_USER, DEFAULT_INITIAL_USER);
	}
	
	@Override
	public void initializeDefaultPreferences() {
        IPreferenceStore store = Preferences.getDefault().getPreferenceStore();
        Map<String, String> initializationEntries = PreferenceSupplier.getInitializationEntries();
        for (Map.Entry<String, String> entry : initializationEntries.entrySet()) {
            store.setDefault(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Gets the initial settings for the preferences.
     * 
     * @return the initial settings
     */
	public static Map<String, String> getInitializationEntries() {
        Map<String, String> entries = new HashMap<String, String>();
        entries.put(EPICS_BASE_DIRECTORY, DEFAULT_EPICS_BASE_DIRECTORY);
        entries.put(EPICS_UTILS_DIRECTORY, DEFAULT_EPICS_UTILS_DIRECTORY);
        entries.put(PYTHON_INTERPRETER_PATH, DEFAULT_PYTHON_INTERPRETER_PATH);
        entries.put(GENIE_PYTHON_DIRECTORY,  DEFAULT_GENIE_PYTHON_DIRECTORY);
        entries.put(ADMINISTRATOR_PASSWORD, DEFAULT_ADMINISTRATOR_PASSWORD);
        entries.put(INITIAL_USER, DEFAULT_INITIAL_USER);
    		
        return entries;
	}
}
