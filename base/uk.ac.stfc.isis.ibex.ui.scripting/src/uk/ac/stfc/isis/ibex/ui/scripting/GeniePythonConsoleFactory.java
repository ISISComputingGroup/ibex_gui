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
			"# Configuring GENIE PYTHON, please wait\n" 
			+ "import sys;sys.executable=''\n" 
			+ "from genie_python.genie_startup import * \n" 
			+ "load_script(None, globals()) \n";
	
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
				
		return createPydevInterpreter(launchAndProcess, null);
	}
}
