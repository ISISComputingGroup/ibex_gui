
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

package uk.ac.stfc.isis.ibex.configserver;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayConfiguration;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayAlerts;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;

/**
 * An interface for displaying information from the configuration
 * server.
 */
public interface Displaying {
	/**
	 * Current configuration details for presentation to the user.
	 * 
	 * @return 
	 *         The current configuration details.
	 */
	ForwardingObservable<DisplayConfiguration> displayCurrentConfig();
	
	/**
	 * Returns a collection of blocks whose value and run-control settings can be displayed in a GUI.
	 * 
	 * @return
	 *         a collection of blocks whose value and run-control settings can be displayed in a GUI.
	 */
	Collection<DisplayBlock> getDisplayBlocks();

	/**
	 * Returns a collection of alert settings that can be displayed in a GUI.
	 * 
	 * @return
	 *         a collection of alert settings that can be displayed in a GUI
	 */
	Collection<DisplayAlerts> getDisplayAlerts();
}
