
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

import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.UIEvents.EventTags;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.python.pydev.shared_interactive_console.console.ui.ScriptConsole;
import org.python.pydev.shared_interactive_console.console.ui.ScriptConsoleManager;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Consoles extends AbstractUIPlugin {

	/**
	 * The plug-in ID.
	 */
	public static final String PLUGIN_ID = "uk.ac.stfc.isis.ibex.ui.scripting"; // $NON-NLS-1$

	/**
	 * The perspective ID.
	 */
	public static final String PERSPECTIVE_ID = "uk.ac.stfc.isis.ibex.ui.scripting.perspective";

	// The shared instance
	private static Consoles plugin;

	private static final GeniePythonConsoleFactory GENIE_CONSOLE_FACTORY = new GeniePythonConsoleFactory();

	private IEclipseContext eclipseContext;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		PyDevConfiguration.configure();

		eclipseContext = EclipseContextFactory.getServiceContext(context);

		// Can't get this via injection. The following works though.
		IEventBroker broker = eclipseContext.get(IEventBroker.class);

		EventHandler handler = new EventHandler() {
			@Override
			public void handleEvent(Event event) {
				Object newValue = event.getProperty(EventTags.NEW_VALUE);

				// only run this, if the NEW_VALUE is a MPerspective
				if (!(newValue instanceof MPerspective)) {
					return;
				}

				if (((MPerspective) newValue).getElementId().equals(PERSPECTIVE_ID)) {
					Consoles.createConsole();
				}
			}
		};

		broker.subscribe(UIEvents.ElementContainer.TOPIC_ALL, handler);
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		ScriptConsoleManager.getInstance().closeAll();
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
	public static void createConsole() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (!plugin.anyActive()) {
					GENIE_CONSOLE_FACTORY.createConsole(Commands.setInstrument());
				}
			}
		});
	}
}
