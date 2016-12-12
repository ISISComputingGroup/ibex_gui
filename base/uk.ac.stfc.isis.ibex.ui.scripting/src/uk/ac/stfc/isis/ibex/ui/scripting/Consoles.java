
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
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.python.pydev.shared_interactive_console.console.ui.ScriptConsole;
import org.python.pydev.shared_interactive_console.console.ui.ScriptConsoleManager;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfoReceiver;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.ui.Utils;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Consoles extends AbstractUIPlugin implements InstrumentInfoReceiver {

	public static final Logger LOG = IsisLog.getLogger("Scripting");
	
    /**
     * The plug-in ID.
     */
	public static final String PLUGIN_ID = "uk.ac.stfc.isis.ibex.ui.scripting"; //$NON-NLS-1$

	// The shared instance
	private static Consoles plugin;

    private GeniePythonConsoleFactory genieConsoleFactory = new GeniePythonConsoleFactory();

    // true if after switching instrument the consoles need to be reopened
    private boolean reopenConsole;
	

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
    public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		PyDevConfiguration.configure();

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
     * @param instrument
     */
    @Override
    public void setInstrument(InstrumentInfo instrument) {
        //
    }

    /**
     * @param instrument
     */
    @Override
    public void preSetInstrument(InstrumentInfo instrument) {
        reopenConsole = false;
        IWorkbenchPage activePage = Utils.getActivePage();
        if (activePage == null) {
            return;
        }
        ScriptConsoleManager.getInstance().closeAll();

        IPerspectiveDescriptor scriptingPerspective =
                PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId(Perspective.ID);
        if (activePage.getPerspective() == scriptingPerspective) {
            reopenConsole = true;
        } else {
            activePage.closePerspective(scriptingPerspective, false, false);
        }
    }

    /**
     * After the instrument has been set then reopen the scripting console.
     * 
     * @param instrument the instrument being switched to
     */
    @Override
    public void postSetInstrument(InstrumentInfo instrument) {
        if (reopenConsole) {
            createConsole();
        }
    }

    /**
     * Create a new pydev console based on the current instrument.
     */
    public void createConsole() {
        genieConsoleFactory.createConsole(Commands.setInstrument());
    }

}
