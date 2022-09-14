
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
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.python.pydev.ast.interpreter_managers.InterpreterManagersAPI;
import org.python.pydev.core.IInterpreterInfo;
import org.python.pydev.core.IInterpreterManager;
import org.python.pydev.debug.newconsole.PydevConsoleConstants;
import org.python.pydev.debug.newconsole.PydevConsoleFactory;
import org.python.pydev.debug.newconsole.PydevConsoleInterpreter;
import org.python.pydev.debug.newconsole.env.PydevIProcessFactory;
import org.python.pydev.debug.newconsole.env.PydevIProcessFactory.PydevConsoleLaunchInfo;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;

/**
 * A factory for creating Genie Python Consoles.
 */
public class GeniePythonConsoleFactory extends PydevConsoleFactory {

	private final NullProgressMonitor monitor = new NullProgressMonitor();

	private static final Logger LOG = IsisLog.getLogger(GeniePythonConsoleFactory.class);

	private static final IJobChangeListener JOB_CHANGE_LISTENER = new JobChangeAdapter() {
		@Override
		public void done(IJobChangeEvent event) {
			// Unfortunately I couldn't find a way to enforce that the string here matches the string
			// that pydev is using, as it is a method local variable so even with reflection tricks
			// I'm not sure we can get at it.
			if (event.getJob().getName() == "Create Interactive Console") {
				Consoles.getDefault().installOutputLengthLimitsOnAllConsoles();
				Consoles.getDefault().installConsoleCountListener();
			}
		}
	};

	/**
	 * Creates the scripting console after configuring the set of default
	 * interpreter commands.
	 * 
	 * @param additionalInitialCommands Additional commands to run on startup
	 */
	public void configureAndCreateConsole(String additionalInitialCommands) {
		setInitialInterpreterCommands();
		createConsole(additionalInitialCommands);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createConsole(String additionalInitialComands) {
		
		Display.getDefault().syncExec(new Runnable() {
		    public void run() {
		    	IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
		    	boolean continueWithNewConsole = true;
		    	if (manager.getConsoles().length >= 1) {
		    		continueWithNewConsole = MessageDialog.openQuestion(Display.getDefault().getActiveShell(), "Duplicate console",
			    			 "Scripting Console already exists. New Console will open on top of the current one. "
		    				+ "Scripts running on current console will still run in the background.\n\n"
			    			+ "You can switch between consoles using \"Display Selected Console\""
			    			+ " button. To terminate a console please click on the red icon from the menu bar.\n\n"
			    			+  "Do you still want to proceed with a new Console?");
		    	}
    
		    	if (continueWithNewConsole) {
		    		try {
						createConsole(createGeniePydevInterpreter(), additionalInitialComands);
					} catch (Exception e) {
						LOG.error(e);
					}
		    	}
		    	
		    }
		    	
		});
	

		// Add a listener so that after a console is created, we install output length limits.
		// This is the only way I found to do this, without relying on some arbitrary timeout.
		Job.getJobManager().addJobChangeListener(JOB_CHANGE_LISTENER);
	}
	
	

	private void setInitialInterpreterCommands() {
		IPreferenceStore pydevDebugPreferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE,
				"org.python.pydev.debug");

		pydevDebugPreferenceStore.setDefault(PydevConsoleConstants.INITIAL_INTERPRETER_CMDS,
				Commands.getInitialisationCommands());
	}

	/**
	 * Creates a Genie Python Interpreter.
	 *
	 * @return Interpreter
	 * @throws Exception
	 *             can throw several different exceptions
	 */
	PydevConsoleInterpreter createGeniePydevInterpreter() throws Exception {
		IInterpreterManager manager = InterpreterManagersAPI.getPythonInterpreterManager();
		IInterpreterInfo interpreterInfo = manager.createInterpreterInfo(PreferenceSupplier.getPythonPath(),
				monitor, false);

		PydevIProcessFactory iprocessFactory = new PydevIProcessFactory();

		PydevConsoleLaunchInfo launchAndProcess = iprocessFactory.createLaunch(manager, interpreterInfo,
				interpreterInfo.getPythonPath(), null, null);

		return createPydevInterpreter(launchAndProcess, null, null);
	}
	
}
