
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

package uk.ac.stfc.isis.ibex.ui.scripting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.io.File;

import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.python.pydev.ui.pythonpathconf.InterpreterNewCustomEntriesAdapter;

import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;

/**
 * The Class PyDevAdditionalInterpreterSettings adds interpreter settings to the pydev class so it will work with epics.
 */
public class PyDevAdditionalInterpreterSettings extends InterpreterNewCustomEntriesAdapter {
	
	private static final String PREFERENCE_STORE_ID_FOR_EPICS_LIBS = "org.csstudio.platform.libs.epics";
	private final IPreferencesService preferenceService;
	private final PreferenceSupplier preferenceSupplier;
	Instrument instrumentBundle;
	
	/**
	 * Instantiates a new pyDev additional interpreter settings.
	 * 
	 * This constructor is usually used for testing only
	 *
	 * @param preferenceService the iPreferences service to use
	 * @param instrumentBundle the instrument bundle to use to get details from
	 */
	public PyDevAdditionalInterpreterSettings(IPreferencesService preferenceService, Instrument instrumentBundle) {
		this.preferenceService = preferenceService;
		this.preferenceSupplier = new PreferenceSupplier(preferenceService);
		this.instrumentBundle = instrumentBundle;
	}
	
	/**
	 * Instantiates a new pyDev additional interpreter settings.
	 * 
	 */
	public PyDevAdditionalInterpreterSettings() {
		this(Platform.getPreferencesService(), Instrument.getInstance());
	}
	
	@Override
	public Collection<String> getAdditionalEnvVariables() {
		List<String> entriesToAdd = new ArrayList<String>();
		
		entriesToAdd.add(pvPrefix());
		entriesToAdd.add(extraPaths());
		entriesToAdd.add("IPYTHONENABLE=True"); //Required so that we can hook into autocomplete for load_script
		entriesToAdd.add("PYDEVD_DISABLE_FILE_VALIDATION=1");  // Required to disable warning about Py3.11+ frozen modules.
		addEpicsEnvironment(entriesToAdd);

		return entriesToAdd;
	}
	
	@Override
	public Collection<String> getAdditionalLibraries() {
		List<String> entriesToAdd = new ArrayList<String>();
				
		entriesToAdd.add(geniePythonPath());
		
		return entriesToAdd;
	}
	
	private String pvPrefix() {
		return "MYPVPREFIX=" + instrumentBundle.currentInstrument().pvPrefix();
	}
	
	private String extraPaths() {
        return "PATH=" + toOSPath(preferenceSupplier.epicsBase()) 
           + File.pathSeparator + toOSPath(preferenceSupplier.epicsUtilsPath())
           + File.pathSeparator + geniePythonDir()
           + File.pathSeparator + System.getenv("PATH");
	}

	private String geniePythonPath() {
		return toOSPath(preferenceSupplier.geniePythonPath());
	}

	private String geniePythonDir() {
		return toOSPath(new File(PreferenceSupplier.getPythonPath()).getParent());
	}
	
	private void addEpicsEnvironment(List<String> entries) {
		for (String entry : epicsEnvironment()) {
			entries.add(entry);
		}
	}
	
	/**
	 *  Get the channel access environment variables from EPICS preferences.
	 *  
	 * @return list of environment variables and their settings
	 */
	public List<String> epicsEnvironment() {
		List<String> epicsEnv = new ArrayList<String>();

        final String addList = preferenceService.getString(PREFERENCE_STORE_ID_FOR_EPICS_LIBS, "addr_list", null, null);
        epicsEnv.add("EPICS_CA_ADDR_LIST=" + addList);
        
        final String autoAddr = preferenceService.getBoolean(PREFERENCE_STORE_ID_FOR_EPICS_LIBS, "auto_addr_list", true, null) ? "YES" : "NO";
        epicsEnv.add("EPICS_CA_AUTO_ADDR_LIST=" + autoAddr);
        
        final String maxArrayBytes =
        		preferenceService.getString(PREFERENCE_STORE_ID_FOR_EPICS_LIBS, "max_array_bytes", "16384", null);
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
