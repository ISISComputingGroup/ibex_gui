
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
		
		return entriesToAdd;
	}
	
	private String pvPrefix() {
		return "MYPVPREFIX=" + Instrument.getInstance().currentInstrument().pvPrefix();
	}
	
	private String epicsBasePath() {
        return "PATH=" + toOSPath(PreferenceSupplier.epicsBase()) + ";" + toOSPath(PreferenceSupplier.epicsUtilsPath())
                + ";" + System.getenv("PATH");
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
