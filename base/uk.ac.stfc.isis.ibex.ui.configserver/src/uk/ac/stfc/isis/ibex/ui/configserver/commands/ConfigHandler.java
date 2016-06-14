
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

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;
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
 * @param <T>
 */
public abstract class ConfigHandler<T> {

	protected static final ConfigServer SERVER = Configurations.getInstance().server();
	protected static final Editing EDITING = Configurations.getInstance().edit();
	
    @Inject
    @Named(IServiceConstants.ACTIVE_SHELL) 
    protected Shell activeShell;
	
	/**
	 * This is an inner anonymous class inherited from SameTypeWriter with added functionality
	 * for disabling the command if the underlying PV cannot be written to.
	 */
	protected final SameTypeWriter<T> configService = new SameTypeWriter<T>() {	
		@Override
		public void onCanWriteChanged(boolean canWrite) {
			//setBaseEnabled(canWrite);
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
}
