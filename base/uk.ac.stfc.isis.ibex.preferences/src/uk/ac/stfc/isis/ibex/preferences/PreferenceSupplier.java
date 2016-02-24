
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

public class PreferenceSupplier extends AbstractPreferenceInitializer {
	
    public static final String EPICS_BASE_DIRECTORY = "epics_base_directory";
    public static final String DEFAULT_EPICS_BASE_DIRECTORY = "c:\\Instrument\\Apps\\EPICS\\base\\master\\bin\\windows-x64";

    public static final String PYTHON_INTERPRETER_PATH = "python_interpreter_path";
    public static final String DEFAULT_PYTHON_INTERPRETER_PATH = "C:\\Instrument\\Apps\\Python\\python.exe";
    
    public static final String GENIE_PYTHON_DIRECTORY = "genie_python_directory";
    public static final String DEFAULT_GENIE_PYTHON_DIRECTORY = "C:\\Instrument\\Apps\\Python\\Lib\\site-packages\\genie_python";
    
    public static final String EPICS_UTILS_DIRECTORY = "epics_utils_directory";
    public static final String DEFAULT_EPICS_UTILS_DIRECTORY = "C:\\Instrument\\Apps\\EPICS_UTILS";

    public static final String PYEPICS_DIRECTORY = "pyepics_directory";
    public static final String DEFAULT_PYEPICS_DIRECTORY = "C:\\Instrument\\Apps\\Python\\Lib\\site-packages\\epics";

    public static final String INSTRUMENT_SCIENTIST_PASSWORD = "instrument_scientist_password";
    public static final String DEFAULT_INSTRUMENT_SCIENTIST_PASSWORD = "reliablebeam";
    
    public static final String ADMINISTRATOR_PASSWORD = "administrator_password";
    public static final String DEFAULT_ADMINISTRATOR_PASSWORD = "asyndriver";  

    public static final String INITIAL_USER = "initial_user";
    public static final String DEFAULT_INITIAL_USER = "Default user";
    
	public static final IScopeContext SCOPE_CONTEXT = InstanceScope.INSTANCE;
    public static final String PREFERENCE_NODE = "uk.ac.stfc.isis.ibex.preferences";
	
	public static IEclipsePreferences getPreferences() {
        return SCOPE_CONTEXT.getNode(PREFERENCE_NODE);
    }
		
	public static String epicsBase() {
		return getPreferences().get(EPICS_BASE_DIRECTORY, DEFAULT_EPICS_BASE_DIRECTORY);
	}
	
	public static String pythonInterpreterPath() {
		return getPreferences().get(PYTHON_INTERPRETER_PATH, DEFAULT_PYTHON_INTERPRETER_PATH);
	}
	
	public static String geniePythonPath() {
		return getPreferences().get(GENIE_PYTHON_DIRECTORY, DEFAULT_GENIE_PYTHON_DIRECTORY);
	}
	
	/**
	 * @return The path to the EPICS_UTIL directory
	 */
	public static String epicsUtilsPath() {
		return getPreferences().get(EPICS_UTILS_DIRECTORY, DEFAULT_EPICS_UTILS_DIRECTORY);
	}

	public static String pyEpicsPath() {
		return getPreferences().get(PYEPICS_DIRECTORY, DEFAULT_PYEPICS_DIRECTORY);
	}

	public static String administratorPassword() {
		return getPreferences().get(ADMINISTRATOR_PASSWORD, DEFAULT_ADMINISTRATOR_PASSWORD);
	}
	
	public static String instrumentScientistPassword() {
		return getPreferences().get(INSTRUMENT_SCIENTIST_PASSWORD, DEFAULT_INSTRUMENT_SCIENTIST_PASSWORD);
	}
	
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

	public static Map<String, String> getInitializationEntries() {
        Map<String, String> entries = new HashMap<String, String>();
        entries.put(EPICS_BASE_DIRECTORY, DEFAULT_EPICS_BASE_DIRECTORY);
        entries.put(EPICS_UTILS_DIRECTORY, DEFAULT_EPICS_UTILS_DIRECTORY);
        entries.put(PYTHON_INTERPRETER_PATH, DEFAULT_PYTHON_INTERPRETER_PATH);
        entries.put(GENIE_PYTHON_DIRECTORY,  DEFAULT_GENIE_PYTHON_DIRECTORY);
        entries.put(PYEPICS_DIRECTORY, DEFAULT_PYEPICS_DIRECTORY);
        entries.put(ADMINISTRATOR_PASSWORD, DEFAULT_ADMINISTRATOR_PASSWORD);
        entries.put(INITIAL_USER, DEFAULT_INITIAL_USER);
    		
        return entries;
	}
}
