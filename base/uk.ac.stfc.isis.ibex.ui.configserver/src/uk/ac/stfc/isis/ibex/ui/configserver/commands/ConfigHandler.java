
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

import org.eclipse.e4.core.di.annotations.CanExecute;

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
     * Abstract method for handling a change in write status.
     * 
     * @param canWrite whether can write or not
     */
	public abstract void canWriteChanged(boolean canWrite);
	
}
