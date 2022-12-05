
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2021 Science & Technology Facilities Council.
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
import java.util.Optional;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import org.apache.commons.lang.SystemUtils;
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
	 * @param preferenceService the preference service to use
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
     * The relative path to the bundled Python.
     */
    private static final String PYTHON_RELATIVE_PATH_WINDOWS = "/resources/Python3/python.exe";
    
    /**
     * The relative path to the bundled Python.
     */
    private static final String PYTHON_RELATIVE_PATH_LINUX = "/resources/Python3/bin/python";
    
    /**
     * The path to the instrument/developer's genie python on windows.
     */
	private static final String DEFAULT_PYTHON_3_INTERPRETER_PATH_WINDOWS = "C:\\Instrument\\Apps\\Python3\\python.exe";
	

    /**
     * The path to the instrument/developer's genie python on linux.
     */
    private static final String DEFAULT_PYTHON_3_INTERPRETER_PATH_LINUX = "/usr/local/ibex/genie_python/bin";
    
	/**
	 * Gets the installed Python, unless it hasn't been bundled and then gets the Python bundled with the gui.
	 * 
	 * @return The string path to the python executable.
	 * @throws IOException if python could not be found.
	 */
	public static String getPythonPath() {
	    String pythonPath, relPath;
	    
	    if (SystemUtils.IS_OS_WINDOWS) {
	        pythonPath = Path.forWindows(DEFAULT_PYTHON_3_INTERPRETER_PATH_WINDOWS).toOSString();
	        relPath = PYTHON_RELATIVE_PATH_WINDOWS;
	    } else {
	        pythonPath = Path.forPosix(DEFAULT_PYTHON_3_INTERPRETER_PATH_LINUX).toOSString();
	        relPath = PYTHON_RELATIVE_PATH_LINUX;
	    }
	    
		if (Files.exists(Paths.get(pythonPath))) {
			LOG.info("getDefaultPythonPath found python at: " + pythonPath);
		} else {
			try {
				pythonPath = relativePathToFull(relPath);
				LOG.info("getDefaultPythonPath found python at: " + pythonPath);
			} catch (IOException e) {
				LOG.error("Bundled Python not found");
			}
			
		}
		return pythonPath;
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
    private static final String DEFAULT_GENIE_PYTHON_DIRECTORY_WINDOWS = "C:\\Instrument\\Apps\\Python3\\Lib\\site-packages\\genie_python";
    private static final String DEFAULT_GENIE_PYTHON_DIRECTORY_LINUX = "/usr/local/ibex/genie_python/lib/python3.6/site-packages";
    
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
     * The default place to store script generator script definition files.
     * This is a relative path to keep the script definitions within the script generator plugin.
     */
    private static final String DEFAULT_SCRIPT_DEFINITIONS_FOLDER = "";
    
    /**
     * Defines where the script definitions repository is kept.
     */
    private static final String SCRIPT_DEFINITIONS_FOLDER = "script_definitions_folder";
    
    /**
     * The default URL for the Script Generator manual page
     */
    private static final String DEFAULT_SCRIPT_GENERATOR_MANUAL_URL = "https://shadow.nd.rl.ac.uk/ibex_user_manual/Using-the-Script-Generator";
    
    /**
     * Defines the URL of the Script Generator page on the user manual
     */
    private static final String SCRIPT_GENERATOR_MANUAL_URL = "script_generator_manual_url";
    
    /**
     * The default of whether to hide the script definition error table or not.
     */
    private static final boolean DEFAULT_HIDE_SCRIPT_DEFINITION_ERRORS = false;
    
    /**
     * Defines whether to hide script definition error table.
     */
    private static final String HIDE_SCRIPT_DEFINITION_ERRORS = "hide_script_definition_error_table";
    
    /**
     * User defined location for temporary folder.
     */
    private static final String USER_TEMP_PATH = "temporary_files_folder";
    
    /**
     * Default location for temporary folder for use by GUI.
     */
    private static final String DEFAULT_USER_TEMP_PATH = Paths.get(System.getProperty("user.home"), "AppData", "Local", "IBEX").toString();
	
    /**
     * Gets a string from the IBEX preference store.
     * 
     * @return the preferences string, or the default if it was not present.
     */
	private String getString(String name, String def) {
		return preferenceService.getString(PREFERENCE_NODE, name, def, null);
	}
	
	/**
     * Gets a boolean from the IBEX preference store.
     * 
     * @return the preferences boolean, or the default if it was not present.
     */
	private boolean getBoolean(String name, boolean def) {
		return preferenceService.getBoolean(PREFERENCE_NODE, name, def, null);
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
	    if (SystemUtils.IS_OS_WINDOWS) {
		return getString(PYTHON_INTERPRETER_PATH, DEFAULT_PYTHON_3_INTERPRETER_PATH_WINDOWS);
            } else { 
		return getString(PYTHON_INTERPRETER_PATH, DEFAULT_PYTHON_3_INTERPRETER_PATH_LINUX);
           }
	}
    
	/**
     * Gets the preference setting for genie_python directory.
     * 
     * @return the setting (uses default if not set)
     */
	public String geniePythonPath() {
	    if (SystemUtils.IS_OS_WINDOWS) {
		return getString(GENIE_PYTHON_DIRECTORY, DEFAULT_GENIE_PYTHON_DIRECTORY_WINDOWS);
            } else {
		return getString(GENIE_PYTHON_DIRECTORY, DEFAULT_GENIE_PYTHON_DIRECTORY_LINUX);
            }
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
	 * Gets the preference location for temporary files.
	 * 
	 * @return Temporary files directory
	 */
	public String tempFilePath() {
		return getString(USER_TEMP_PATH, DEFAULT_USER_TEMP_PATH);
	}
	
	/**
	 * Whether the values of invalid blocks should be shown.
	 * 
	 * @return true if invalid blocks should be shown with their current value and the relevant alarm border, 
	 *  false if invalid blocks should be shown with placeholder text and an alarm border
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
     * Gets a list of the folders containing script generator script definitions.
     * To implement many separate with commas (this is handled in the Python side).
     * 
     * @return a list of of folders paths that contain script generator script definitions.
     */
	public Optional<String> scriptGeneratorScriptDefinitionFolder() {
		Optional<String> scriptDefinitionsPath;
		String scriptDefinitionsPathPreference = getString(SCRIPT_DEFINITIONS_FOLDER, DEFAULT_SCRIPT_DEFINITIONS_FOLDER);
		if (scriptDefinitionsPathPreference.equals(DEFAULT_SCRIPT_DEFINITIONS_FOLDER)) {
			// Default is blank, return empty optional
			scriptDefinitionsPath = Optional.empty();
		} else {
			scriptDefinitionsPath = Optional.of(scriptDefinitionsPathPreference);
		}
		return scriptDefinitionsPath;
		
	}
	
    /**
     * Get a list of URLs pointing to the Script Generator page on the user manual.
     * 
     * @return a comma-separated list of URLs
     */
    public String scriptGeneratorManualURL() {
        return getString(SCRIPT_GENERATOR_MANUAL_URL, DEFAULT_SCRIPT_GENERATOR_MANUAL_URL);
    }
		
	/** 
	 * Get whether to hide the script gen script definition error table.
	 * 
	 * @return true if we should hide the table, false if not.
	 */
	public boolean hideScriptGenScriptDefinitionErrorTable() {
		return getBoolean(HIDE_SCRIPT_DEFINITION_ERRORS, DEFAULT_HIDE_SCRIPT_DEFINITION_ERRORS);
	}
}
