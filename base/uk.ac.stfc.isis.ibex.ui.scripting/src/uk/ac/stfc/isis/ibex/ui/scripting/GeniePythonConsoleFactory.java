
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

import org.apache.logging.log4j.Logger;
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

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;

/**
 * A factory for creating Genie Python Consoles.
 * 
 * This factory creates the pydev console in a new tab.
 * 
 * To avoid issues with "empty" part stacks, the python console is currently
 * created as follows: - The e4 xmi defines *two* part stacks, the first of
 * which is invisible - In the first (invisible) part stack there is a view
 * which calls this class. - This class creates a PyDev console tab and it gets
 * added to the second (visible) part stack - To avoid errors with the second
 * part stack being empty, we make it contain an Empty view (which is neither
 * visible nor rendered).
 * 
 * This is all very hacky.
 * 
 * TODO: As part of the E4 migration we should change this so that we don't need
 * the mess of invisible parts to make the console appear in a sensible way. See
 * if we can simply wrap the pydev console class and pass it the startup
 * parameters we need (i.e. default commands).
 * 
 */
public class GeniePythonConsoleFactory extends PydevConsoleFactory {

	private final NullProgressMonitor monitor = new NullProgressMonitor();

    private static final Logger LOG = IsisLog.getLogger(GeniePythonConsoleFactory.class);

	@Override
	public void createConsole(String additionalInitialComands) {
        try {
			setInitialInterpreterCommands();
			super.createConsole(createGeniePydevInterpreter(), additionalInitialComands);
		} catch (Exception e) {
            LOG.error(e);
		}
	}

	private void setInitialInterpreterCommands() {
		IPreferenceStore pydevDebugPreferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, "org.python.pydev.debug");
		String commands = pydevDebugPreferenceStore.getDefaultString(PydevConsoleConstants.INITIAL_INTERPRETER_CMDS);
		if (commands == null || commands.contains(Commands.GENIE_INITIALISATION)) {
			return;
		}

		pydevDebugPreferenceStore.setDefault(PydevConsoleConstants.INITIAL_INTERPRETER_CMDS, Commands.GENIE_INITIALISATION);
	}

    PydevConsoleInterpreter createGeniePydevInterpreter() throws Exception {
		IInterpreterManager manager = PydevPlugin.getPythonInterpreterManager(true);
		IInterpreterInfo interpreterInfo = manager.createInterpreterInfo(PreferenceSupplier.pythonInterpreterPath(), monitor, false);

        PydevIProcessFactory iprocessFactory = new PydevIProcessFactory();

		PydevConsoleLaunchInfo launchAndProcess = 
				iprocessFactory.createLaunch(manager, interpreterInfo, interpreterInfo.getPythonPath(), null, null);

		return createPydevInterpreter(launchAndProcess, null, null);
	}
}
