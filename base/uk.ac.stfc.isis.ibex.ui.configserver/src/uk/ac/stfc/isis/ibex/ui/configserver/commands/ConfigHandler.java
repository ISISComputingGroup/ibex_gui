
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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.Editing;
import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

/**
 * This class forms the basis of any "commands" used in relation to the ConfigServer.
 * The ConfigServer is the plug-in that interacts with the BlockServer.
 * 
 * Commands are used by the Eclipse framework to contribute actions to the user interface 
 * (e.g. menus, context menus etc.)
 *
 * @param <T> The type of data expected from the underlying PV
 */
public abstract class ConfigHandler<T> extends AbstractHandler {
    /** The configuration server object. */
	protected static final ConfigServer SERVER = Configurations.getInstance().server();
    /** The object for editing a configuration. */
	protected static final Editing EDITING = Configurations.getInstance().edit();

	/**
	 * This is an inner anonymous class inherited from SameTypeWriter with added functionality
	 * for modifying the command if the underlying PV cannot be written to.
	 */
	protected final SameTypeWriter<T> configService = new SameTypeWriter<T>() {	
		@Override
		public void onCanWriteChanged(boolean canWrite) {
			canWriteChanged(canWrite);
		};	
	};
	
	/**
	 * Constructor.
	 * 
	 * @param destination where to write the data to
	 */
	public ConfigHandler(Writable<T> destination) {
		configService.writeTo(destination);
		destination.subscribe(configService);
	}
	
    /**
     * Returns the shell for the active window.
     * 
     * @return the shell
     */
	protected Shell shell() {
		return activeWindow().getShell();
	}
	
    /**
     * Returns the active workbench window.
     * 
     * @return the window
     */
	protected IWorkbenchWindow activeWindow() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	}
	
    /**
     * Abstract method for handling a change in write status.
     * 
     * @param canWrite whether can write or not
     */
	public abstract void canWriteChanged(boolean canWrite);
	
    /**
     * Implements the required eclipse interface.
     * 
     * Declared as final here, implement executeAndCatchErrors instead so that
     * we can "hook into" any errors that occur in any of these dialogs and do
     * something sensible with them.
     * 
     * @param event
     *            the execution event
     * @return null (ignored by eclipse framework)
     */
    @Override
    public final Object execute(ExecutionEvent event) {
        try {
            safeExecute(event);
        } catch (RuntimeException e) {
            onError(e);
        }
        return null;
    }

    /**
     * Executes an eclipse event. Any runtime errors thrown by this method will
     * be caught by the execute() method and cause the onError() handler to be
     * called.
     * 
     * @param event
     *            the eclipse event
     */
    public abstract void safeExecute(ExecutionEvent event);

    /**
     * Error handler. Override this to provide specific error handling
     * behaviour.
     * 
     * @param ex
     *            the exception that was thrown from the handler
     */
    public void onError(Exception ex) {
        Logger.getGlobal().log(Level.SEVERE, "Exception while executing configserver command", ex);

        MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error",
                ex.getMessage());
    }
	
}
