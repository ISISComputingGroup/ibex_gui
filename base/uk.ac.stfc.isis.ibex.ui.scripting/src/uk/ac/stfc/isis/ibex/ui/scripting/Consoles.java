
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

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.Logger;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.UIEvents.EventTags;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.internal.console.ConsoleView;
import org.eclipse.ui.internal.console.OpenConsoleAction;
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
@SuppressWarnings("restriction")
public class Consoles extends AbstractUIPlugin {

	/**
	 * The plug-in ID.
	 */
	public static final String PLUGIN_ID = "uk.ac.stfc.isis.ibex.ui.scripting"; // $NON-NLS-1$

	/**
	 * The ID for the scripting perspective.
	 */
	public static final String SCRIPTING_PERSPECTIVE_ID = "uk.ac.stfc.isis.ibex.ui.scripting.perspective";
	
	/**
	 * The ID for the reflectometry perspective.
	 */
	public static final String REFL_PERSPECTIVE_ID = "uk.ac.stfc.isis.ibex.client.e4.product.perspective.reflectometry";

	// The shared instance
	private static Consoles plugin;

	private static final GeniePythonConsoleFactory GENIE_CONSOLE_FACTORY = new GeniePythonConsoleFactory();

	/**
	 * This is the file which we will log old console output to if it is trimmed automatically.
	 */
	private static final String TRIMMED_CONSOLE_LOG_PATH = Paths.get(System.getProperty("user.dir"), "console-output-trimmed.txt").toString();
	
	private IEclipseContext eclipseContext;
	/**
	 * Clear console menu item ID.
	 */
	private final String CLEAR_CONSOLE_ID = "uk.ac.stfc.isis.ibex.ui.scripting.clearConsole";

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
				String id = ((MPerspective) newValue).getElementId();
				if (id.equals(SCRIPTING_PERSPECTIVE_ID) || id.equals(REFL_PERSPECTIVE_ID)) {
					createConsole(id);
					editToolbarItems();
				}
			}
		};
		editToolbarItems();
		broker.subscribe(UIEvents.ElementContainer.TOPIC_ALL, handler);

		GraphingConnector.startListening();
		
	}
	
	private void editToolbarItems() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				ConsoleView view = getConsoleView();	
				if (view != null) {
					IToolBarManager tbm = view.getViewSite().getActionBars().getToolBarManager();
					IContributionItem [] items= tbm.getItems();
					// Add our "Open Console" action
					GenieOpenConsoleAction openConsoleAction = new GenieOpenConsoleAction();
					tbm.insertBefore(CLEAR_CONSOLE_ID, openConsoleAction);
					
					// Console view icons that are not required. We are removing OpenConsoleAction so that we can add our Action
					// and make it behave the way we want. In this case we remove Pin Console and Open Console icons.
					List<ActionContributionItem> itemsToRemove = Arrays.stream(items).filter(item->(item instanceof ActionContributionItem))
 					.map(item->(ActionContributionItem)item)
					.filter(item-> item.getAction().toString().contains("PinConsole")||
							item.getAction() instanceof OpenConsoleAction)
					.collect(Collectors.toList());
					
					itemsToRemove.forEach(action -> tbm.remove(action));
					view.getViewSite().getActionBars().updateActionBars();
				}
			}
		});
		
	}
	
	/**
	 * Gets Console view
	 * @return current console view
	 */
	private ConsoleView getConsoleView() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		IViewPart part = page.findView(IConsoleConstants.ID_CONSOLE_VIEW);
		return (ConsoleView)part;
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
	 * 
	 * @param perspectiveId The id for the perspective to create the console on.
	 */
	public void createConsole(String perspectiveId) {
		boolean compactPlot = perspectiveId.equals(REFL_PERSPECTIVE_ID) ? true : false;
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				if (!anyActive()) {
					LOG.info("No scripting consoles active; creating new scripting console.");
					// Adding this forces the console view to be created in the correct perspective.
					// Even if PyDEV takes a few seconds to initialise it's console and the perspective
					// is switched in the meantime, the console will still be created in the correct place.
					//
					// Unfortunately it does cause eclipse to throw an exception (in a different thread) as
					// ConsoleManager calls:
					//     consoleView.getSite()
					// which, in E4, returns null and so throws a NullPointerException. We can't catch this
					// exception as it is in a worker thread. Nevertheless, this is better than the confusion 
					// that follows the scripting console getting accidentally opened in the wrong perspective.
					new ConsoleView();
					
  					GENIE_CONSOLE_FACTORY.configureAndCreateConsole(Commands.getSetInstrumentCommand(), compactPlot);
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
						LOG.info("Too much output (more than " + MAXIMUM_CHARACTERS_TO_KEEP_PER_CONSOLE + " characters). Saving & clearing the output of this console.");

						writeConsoleOutputToFile(console);
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

	private void writeConsoleOutputToFile(ScriptConsole console) {
		try (FileWriter writer = new FileWriter(TRIMMED_CONSOLE_LOG_PATH)) {
			writer.write(console.getDocument().get());
			LOG.info(String.format("Console output saved to file at '%s'", TRIMMED_CONSOLE_LOG_PATH));
		} catch (RuntimeException | IOException e) {
			LoggerUtils.logErrorWithStackTrace(LOG, e.getMessage(), e);
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
