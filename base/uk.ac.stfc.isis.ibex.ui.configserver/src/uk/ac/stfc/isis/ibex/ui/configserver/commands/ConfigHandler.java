
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

package uk.ac.stfc.isis.ibex.ui.configserver.commands;


import java.util.Map;
import java.util.Set;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.Editing;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.editing.DuplicateChecker;
import uk.ac.stfc.isis.ibex.epics.writing.OnCanWriteChangeListener;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * This class forms the basis of any "commands" used in relation to the ConfigServer.
 * The ConfigServer is the plug-in that interacts with the BlockServer.
 *
 * Commands are used by the Eclipse framework to contribute actions to the user interface
 * (e.g. menus, context menus etc.)
 *
 * @param <T> The type of data expected from the underlying PV
 */
public abstract class ConfigHandler<T> {
    /** The configuration server object. */
	protected static final ConfigServer SERVER = Configurations.getInstance().server();
    /** The object for editing a configuration. */
	protected static final Editing EDITING = Configurations.getInstance().edit();
	/** can execute the handler */
	private boolean canExecute;

	/**
	 * The Handler can be executed.
	 * @param canExecute true if it can be executed; false otherwise
	 */
	protected void setCanExecute(boolean canExecute) {
		this.canExecute = canExecute;
	}

	/**
	 *
	 * @return whether the handler can be executed
	 */
	@CanExecute
	public boolean canExecute() {
		return this.canExecute;
	}

	/**
	 * The destination writable, to which commands executed by this class will be sent.
	 */
	protected final Writable<T> configService;
	
	private OnCanWriteChangeListener canWriteListener = canWrite -> canWriteChanged(canWrite);

	/**
	 * Constructor.
	 *
	 * @param destination where to write the data to
	 */
	public ConfigHandler(Writable<T> destination) {
	    destination.addOnCanWriteChangeListener(canWriteListener);
		configService = destination;
	}

    /**
     * Abstract method for handling a change in write status.
     *
     * @param canWrite whether can write or not
     */
	public abstract void canWriteChanged(boolean canWrite);

    /**
     * Execute the handler and catch any errors to display to the user.
     *
     * Declared as final here, implement executeAndCatchErrors instead so that
     * we can "hook into" any errors that occur in any of these dialogs and do
     * something sensible with them.
     *
     * @param shell
     *            the shell to use for the dialogue
     */
    @Execute
    public final void execute(Shell shell) {
        try {
            safeExecute(shell);
        } catch (Exception e) {
            onError(e);
        }
    }

    /**
     * Executes an eclipse event. Any runtime errors thrown by this method will
     * be caught by the execute() method and cause the onError() handler to be
     * called.
     *
     * @param event
     *            the eclipse event
     * @throws Exception
     *             thrown if there is an error in executing the command.
     */
    public abstract void safeExecute(Shell event) throws Exception;

    /**
     * Error handler. Override this to provide specific error handling
     * behaviour.
     *
     * @param ex
     *            the exception that was thrown from the handler
     */
    public void onError(Exception ex) {
        LoggerUtils.logErrorWithStackTrace(IsisLog.getLogger(getClass()), "Exception while executing configserver command", ex);

        MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                "Error",
                "An error occured in the IBEX user interface while performing that action.\n\n"
                 + "Please report the problem to the computing group with a description of what you were trying to do.");
    }

    /**
     * Gets the item conflicts on load with the given Duplicate checker and configuration.
     * @param checker the DuplicateChecker
     * @param config the Configuration
     * @return a map of conflicts
     */
    protected Map<String, Set<String>> getItemConflicts(DuplicateChecker<?> checker, Configuration config) {
        checker.setBase(config);
        return checker.checkItemsOnLoad();
    }
}
