
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;

/**
 * Supplies the details for the IBEX preference page.
 */
public class PreferenceSupplier {
	
	private final IPreferencesService preferenceService;
	
	public PreferenceSupplier() {
		this(Platform.getPreferencesService());
	}
	
	public PreferenceSupplier(IPreferencesService preferenceService) {
		this.preferenceService = preferenceService;
	}
	
    /**
     * The preference setting for the location of EPICS base.
     */
    private static final String EPICS_BASE_DIRECTORY = "epics_base_directory";

    /**
     * The default for the location of EPICS base.
     */
    private static final String DEFAULT_EPICS_BASE_DIRECTORY = "c:\\Instrument\\Apps\\EPICS\\base\\master\\bin\\windows-x64";

    /**
     * The preference setting for the location of Python.
     */
    private static final String PYTHON_INTERPRETER_PATH = "python_interpreter_path";

    /**
     * The default for the location of Python.
     */
    private static final String DEFAULT_PYTHON_INTERPRETER_PATH = "C:\\Instrument\\Apps\\Python\\python.exe";
    
    /**
     * The preference setting for the location of genie_python.
     */
    private static final String GENIE_PYTHON_DIRECTORY = "genie_python_directory";

    /**
     * The default for the location of genie_python.
     */
    private static final String DEFAULT_GENIE_PYTHON_DIRECTORY = "C:\\Instrument\\Apps\\Python\\Lib\\site-packages\\genie_python";
    
    /**
     * The preference setting for the location of EPICS utils.
     */
    private static final String EPICS_UTILS_DIRECTORY = "epics_utils_directory";

    /**
     * The default for the location of EPICS utils.
     */
    private static final String DEFAULT_EPICS_UTILS_DIRECTORY = "C:\\Instrument\\Apps\\EPICS_UTILS";

    /**
     * The overall preferences name.
     */
    private static final String PREFERENCE_NODE = "uk.ac.stfc.isis.ibex.preferences";
    
    /**
     * Defines which perspectives to hide.
     */
    private static final String PERSPECTIVES_TO_HIDE = "perspectives_not_shown";
    
    /**
     * Defines which perspectives to hide by default.
     */
    private static final String DEFAULT_PERSPECTIVES_TO_HIDE = "";
	
    /**
     * Gets a string from the IBEX preference store.
     * 
     * @return the preferences string, or the default if it was not present.
     */
	private String getString(String name, String def) {
		return preferenceService.getString(PREFERENCE_NODE, name, def, null);
	}
		
    /**
     * Gets the preference setting for EPICS base directory.
     * 
     * @return the setting (uses default if not set)
     */
	public String epicsBase() {
		return getString(EPICS_BASE_DIRECTORY, DEFAULT_EPICS_BASE_DIRECTORY);
	}
	
    /**
     * Gets the preference setting for Python directory.
     * 
     * @return the setting (uses default if not set)
     */
	public String pythonInterpreterPath() {
		return getString(PYTHON_INTERPRETER_PATH, DEFAULT_PYTHON_INTERPRETER_PATH);
	}
	
    /**
     * Gets a list of perspective IDs which should not be shown.
     * 
     * @return a list of perspective IDs which should not be shown (may be empty, but never null).
     */
	public List<String> perspectivesToHide() {
		String preferencesString = getString(PERSPECTIVES_TO_HIDE, DEFAULT_PERSPECTIVES_TO_HIDE);
		if (preferencesString == null || preferencesString.isEmpty()) {
			return new ArrayList<>();
		}
		return Arrays.asList(preferencesString.split(",")).stream().map(String::trim).collect(Collectors.toList());
	}
	
	/**
     * Gets the preference setting for genie_python directory.
     * 
     * @return the setting (uses default if not set)
     */
	public String geniePythonPath() {
		return getString(GENIE_PYTHON_DIRECTORY, DEFAULT_GENIE_PYTHON_DIRECTORY);
	}
	
	/**
     * Gets the preference for the EPICS_UTIL directory.
     * 
     * @return the setting (uses default if not set)
     */
	public String epicsUtilsPath() {
		return getString(EPICS_UTILS_DIRECTORY, DEFAULT_EPICS_UTILS_DIRECTORY);
	}
}
