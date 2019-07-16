
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.logging.log4j.Logger;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.UIEvents.EventTags;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.python.pydev.shared_interactive_console.console.ui.ScriptConsole;
import org.python.pydev.shared_interactive_console.console.ui.ScriptConsoleManager;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;
import uk.ac.stfc.isis.ibex.ui.graphing.GraphingConnector;

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
	 * Limit on the total number of lines (input and output) per console.
	 *
	 * Limit is based on the following:
	 * - 5M characters ~ 500MB memory use (measured using visual VM)
	 * - Users could have multiple consoles open (but are unlikely to have more than a few with significant output)
	 * - We want to keep the memory use of the consoles less than ~100MB
	 */
	public static final int MAXIMUM_CHARACTERS_TO_KEEP_PER_CONSOLE = 1000000;

	private static final Logger LOG = IsisLog.getLogger(Consoles.class);

	private Set<Runnable> consoleLengthListeners = new HashSet<Runnable>();

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
					createConsole();
				}
			}
		};

		broker.subscribe(UIEvents.ElementContainer.TOPIC_ALL, handler);

		GraphingConnector.startListening();
	}

	/**
	 * Returns a stream of python scripting consoles (excluding other console types).
	 *
	 * If no consoles are open, an empty stream will be returned.
	 *
	 * @return a stream of open scripting consoles.
	 */
	private Stream<ScriptConsole> getPythonScriptingConsoles() {
		return Arrays.stream(ConsolePlugin.getDefault()
				.getConsoleManager()
				.getConsoles())
				.filter(ScriptConsole.class::isInstance)
				.map(ScriptConsole.class::cast);
	}

	/**
	 * Are there any active console open.
	 *
	 * @return true, if there are; false otherwise
	 */
	public boolean anyActive() {
		return getPythonScriptingConsoles().findAny().isPresent();
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
	public void createConsole() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (!anyActive()) {
					LOG.info("No scripting consoles active; creating new scripting console.");
					GENIE_CONSOLE_FACTORY.createConsole(Commands.setInstrument());
				}
			}
		});
	}

	/**
	 * Installs an output length limiting listener on all open consoles.
	 *
	 * The consoles will be cleared if their total output exceeds the limit defined at the top of this file.
	 */
	public void installOutputLengthLimitsOnAllConsoles() {
		LOG.info("Installing output length limits on all consoles");
		getPythonScriptingConsoles().forEach(this::installOutputLengthLimit);
	}

	private void installOutputLengthLimit(final ScriptConsole console) {
		try {
			console.getDocument().addDocumentListener(new IDocumentListener() {
				@Override
				public void documentChanged(DocumentEvent event) {
				}

				@Override
				public void documentAboutToBeChanged(DocumentEvent event) {
					if (console.getDocument().getLength() > MAXIMUM_CHARACTERS_TO_KEEP_PER_CONSOLE) {
						LOG.info("Too much output (more than " + MAXIMUM_CHARACTERS_TO_KEEP_PER_CONSOLE + " characters). Clearing the output of this console.");
						Display.getDefault().asyncExec(console::clearConsole);
					}
					consoleLengthListeners.forEach(Runnable::run);
				}
			});
			LOG.info(String.format("Added output length limit to console %s", console));
		} catch (RuntimeException e) {
			// If we can't add the listener, log and carry on, this is not a critical error.
			LoggerUtils.logErrorWithStackTrace(LOG,
					String.format("Failed to install output length limit to console '%s' because: %s", console, e.getMessage()), e);
		}
	}

	/**
	 * Register a listener to be called whenever the length of the console output changes.
	 * @param r a runnable to run whenever the length changes.
	 */
	public void addConsoleLengthListener(Runnable r) {
		consoleLengthListeners.add(r);
	}

	/**
	 * Removes a console length listener that was previously registered.
	 * @param r the runnable to remove
	 */
	public void removeConsoleLengthListener(Runnable r) {
		consoleLengthListeners.remove(r);
	}
}
