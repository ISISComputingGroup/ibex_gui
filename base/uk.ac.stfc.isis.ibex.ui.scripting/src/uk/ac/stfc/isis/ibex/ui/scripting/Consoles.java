
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

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.python.pydev.shared_interactive_console.console.ui.ScriptConsole;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Consoles extends AbstractUIPlugin {
	
    /**
     * The plug-in ID.
     */
    public static final String PLUGIN_ID = "uk.ac.stfc.isis.ibex.ui.scripting"; // $NON-NLS-1$

	// The shared instance
	private static Consoles plugin;

    private GeniePythonConsoleFactory genieConsoleFactory;

    // true if after switching instrument the consoles need to be reopened
    private boolean reopenConsole;
	
    public Consoles() {

    }

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
    public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		PyDevConfiguration.configure();
        genieConsoleFactory = new GeniePythonConsoleFactory();
	}

    /**
     * Are there any active console open.
     *
     * @return true, if there are; false otherwise
     */
	public boolean anyActive() {
		for (IConsole console : ConsolePlugin.getDefault().getConsoleManager().getConsoles()) {
			if (console instanceof ScriptConsole) {
				return true;
			}
		}
		
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
    public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
     * Returns the shared instance.
     *
     * @return the shared instance
     */
	public static Consoles getDefault() {
		return plugin;
	}

    /**
     * Create a new pydev console based on the current instrument.
     */
    public void createConsole() {
        genieConsoleFactory.createConsole(Commands.setInstrument());
    }

}
