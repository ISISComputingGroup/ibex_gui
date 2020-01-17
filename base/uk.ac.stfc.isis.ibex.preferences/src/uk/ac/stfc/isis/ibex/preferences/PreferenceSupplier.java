
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

/**
 * Supplies the details for the IBEX preference page.
 */
public class PreferenceSupplier {
	
	private static final Logger LOG = IsisLog.getLogger(PreferenceSupplier.class);
	
	private final IPreferencesService preferenceService;
	
	/**
	 * Instantiate a new preference supplier.
	 */
	public PreferenceSupplier() {
		this(Platform.getPreferencesService());
	}
	
	/**
	 * Instantiate a new preference supplier based on a specific preferenceService.
	 * @param preferenceService the preference serice to use
	 */
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
     * The relative path to python.
     */
    private static final String PYTHON_RELATIVE_PATH = "/resources/Python3/python.exe";
    
    /**
     * The path to the developer's genie python.
     */
	private static final String DEFAULT_PYTHON_3_INTERPRETER_PATH = "C:/Instrument/Apps/Python3/python.exe";
	
	/**
     * The default for the location of Python.
     */
    private static final String DEFAULT_PYTHON_2_INTERPRETER_PATH = "C:\\Instrument\\Apps\\Python\\python.exe";

	
	/**
	 * Gets the python that's been bundled with the gui, unless it hasn't been bundled and then gets the dev python.
	 * 
	 * @return The string path to the python executable.
	 * @throws IOException if python could not be found.
	 */
	public static String getBundledPythonPath() {
		try {
			String pythonPath = relativePathToFull(PYTHON_RELATIVE_PATH);
			LOG.info("getDefaultPythonPath found python at: " + pythonPath);
			return relativePathToFull(PYTHON_RELATIVE_PATH);
		} catch (IOException e) {
			String pythonPath = Path.forWindows(DEFAULT_PYTHON_3_INTERPRETER_PATH).toOSString();
			LOG.info("getDefaultPythonPath found python at: " + pythonPath);
			return pythonPath;
		}
	}

	
	/**
	 * Gets the full path to a file given the path relative to this plugin.
	 * 
	 * @param relativePath The path of the file relative to this plugin.
	 * @return The full path.
	 * @throws IOException if the file could not be found.
	 */
	private static String relativePathToFull(String relativePath) throws IOException {
		try {
			URL resourcePath = PreferenceSupplier.class.getResource(relativePath);
			String fullPath = FileLocator.resolve(resourcePath).getPath();
			return Path.forWindows(fullPath).toOSString();
		} catch (NullPointerException e) {
			throw new IOException("Cannot find python on relative path: " + relativePath);
		}
	}
    
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
     * Defines whether to show the values of blocks in an invalid alarm.
     * True means show the value, False means show N/A
     */
    private static final String SHOW_VALUES_OF_INVALID_BLOCKS = "show_values_of_invalid_blocks";
    
    /**
     * The default place to generate scripts to.
     */
    private static final String DEFAULT_SCRIPT_GENERATION_FOLDER = "C:/Scripts/";
    
    /**
     * Defines where to generate scripts to.
     */
    private static final String SCRIPT_GENERATION_FOLDER = "script_generation_folder";
    
    /**
     * The default place to store script generator configuration files.
     */
    private static final String DEFAULT_SCRIPT_GENERATOR_CONFIG_FOLDER = "C:/ScriptGeneratorConfigs/";
    
    /**
     * Defines where to generate scripts to.
     */
    private static final String SCRIPT_GENERATOR_CONFIG_FOLDER = "script_generator_config_folder";
	
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
		return getString(PYTHON_INTERPRETER_PATH, DEFAULT_PYTHON_2_INTERPRETER_PATH);
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
	
	/**
	 * Whether the values of invalid blocks should be shown
	 * @return true if invalid blocks should be shown with their current value and the relevant alarm border, 
	 * false if invalid blocks should be shown with placeholder text and an alarm border
	 */
	public boolean showInvalidBlockValues() {
		return preferenceService.getBoolean(PREFERENCE_NODE, SHOW_VALUES_OF_INVALID_BLOCKS, false, null);
	}
	
	/**
	 * Get the preference for the folder to generate scripts to.
	 * 
	 * @return The folder to generate scripts to.
	 */
	public String scriptGenerationFolder() {
		return getString(SCRIPT_GENERATION_FOLDER, DEFAULT_SCRIPT_GENERATION_FOLDER);
	}
	
	 /**
     * Gets a list of the folders containing script generator configs.
     * To implement many separate with commas (this is handled in the Python side).
     * 
     * @return a list of of folders paths that contain script generator configs.
     */
	public String scriptGeneratorConfigFolders() {
		return getString(SCRIPT_GENERATOR_CONFIG_FOLDER, DEFAULT_SCRIPT_GENERATOR_CONFIG_FOLDER);
	}
}
