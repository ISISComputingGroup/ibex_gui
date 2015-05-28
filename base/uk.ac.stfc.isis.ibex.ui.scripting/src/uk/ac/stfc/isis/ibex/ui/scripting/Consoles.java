package uk.ac.stfc.isis.ibex.ui.scripting;

import org.apache.logging.log4j.Logger;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.python.pydev.shared_interactive_console.console.ui.ScriptConsole;
import org.python.pydev.shared_interactive_console.console.ui.ScriptConsoleManager;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * The activator class controls the plug-in life cycle
 */
public class Consoles extends AbstractUIPlugin {

	public static final Logger LOG = IsisLog.getLogger("Scripting");
	
	// The plug-in ID
	public static final String PLUGIN_ID = "uk.ac.stfc.isis.ibex.ui.scripting"; //$NON-NLS-1$

	// The shared instance
	private static Consoles plugin;

	private GeniePythonConsoleFactory genieConsoleFactory;
	

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		PyDevConfiguration.configure();

		genieConsoleFactory = new GeniePythonConsoleFactory();
	}

	public void createConsole() {
		genieConsoleFactory.createConsole(Commands.setInstrument());
	}
	
	public boolean anyActive() {
		for (IConsole console : ConsolePlugin.getDefault().getConsoleManager().getConsoles()) {
			if (console instanceof ScriptConsole) {
				return true;
			}
		}
		
		return false;
	}
	
	public void closeAll() {
		ScriptConsoleManager.getInstance().closeAll();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Consoles getDefault() {
		return plugin;
	}

}
