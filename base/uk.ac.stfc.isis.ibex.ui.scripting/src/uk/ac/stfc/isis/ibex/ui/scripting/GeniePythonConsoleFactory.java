
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

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.python.pydev.core.IInterpreterInfo;
import org.python.pydev.core.IInterpreterManager;
import org.python.pydev.debug.newconsole.PydevConsoleConstants;
import org.python.pydev.debug.newconsole.PydevConsoleFactory;
import org.python.pydev.debug.newconsole.PydevConsoleInterpreter;
import org.python.pydev.debug.newconsole.env.PydevIProcessFactory;
import org.python.pydev.debug.newconsole.env.PydevIProcessFactory.PydevConsoleLaunchInfo;
import org.python.pydev.plugin.PydevPlugin;

import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;

public class GeniePythonConsoleFactory extends PydevConsoleFactory {
	
	private final NullProgressMonitor monitor = new NullProgressMonitor();
	
	private final static String GENIE_INITIALISATION = 
 "# Configuring genie_python, please wait\n"
			+ "import sys;sys.executable=''\n" 
			+ "from genie_python.genie_startup import * \n" 
			+ "load_script(None, globals()) \n"
			+ "%matplotlib qt4 \n";
	
	@Override
	public void createConsole(String additionalInitialComands) {
		try {			
			setInitialInterpreterCommands();
			super.createConsole(createGeniePydevInterpreter(), additionalInitialComands);
		} catch (Exception e) {
			Consoles.LOG.error(e);
		}
	}
	
	private void setInitialInterpreterCommands() {
		IPreferenceStore pydevDebugPreferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, "org.python.pydev.debug");
		String commands = pydevDebugPreferenceStore.getDefaultString(PydevConsoleConstants.INITIAL_INTERPRETER_CMDS);
		if (commands == null || commands.contains(GENIE_INITIALISATION)) {
			return;
		}
		
		pydevDebugPreferenceStore.setDefault(PydevConsoleConstants.INITIAL_INTERPRETER_CMDS, GENIE_INITIALISATION);
	}

	private PydevConsoleInterpreter createGeniePydevInterpreter() throws Exception {
		IInterpreterManager manager = PydevPlugin.getPythonInterpreterManager(true);
		IInterpreterInfo interpreterInfo = manager.createInterpreterInfo(PreferenceSupplier.pythonInterpreterPath(), monitor, false);
		
        PydevIProcessFactory iprocessFactory = new PydevIProcessFactory();
        
		PydevConsoleLaunchInfo launchAndProcess = 
				iprocessFactory.createLaunch(manager, interpreterInfo, interpreterInfo.getPythonPath(), null, null);
				
		return createPydevInterpreter(launchAndProcess, null, null);
	}
}
