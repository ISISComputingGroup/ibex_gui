package uk.ac.stfc.isis.ibex.ui.scripting;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.python.pydev.core.IInterpreterInfo;
import org.python.pydev.core.IInterpreterManager;
import org.python.pydev.plugin.PydevPlugin;

import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;

public final class PyDevConfiguration {
	
	private PyDevConfiguration() { }
	
	public static void configure() {
		IInterpreterManager iMan = PydevPlugin.getPythonInterpreterManager(true);
		NullProgressMonitor monitor = new NullProgressMonitor();
		IInterpreterInfo interpreterInfo = iMan.createInterpreterInfo(PreferenceSupplier.pythonInterpreterPath(), monitor, false);
		iMan.setInfos(new IInterpreterInfo[]{interpreterInfo}, null, null);
	}
}
