
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

import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.model.SetCommand;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * A class enabling control of and IOC.
 *
 */
public class IocControl {

    private final UpdatedObservableAdapter<Collection<IocState>> iocs;
	private final SetCommand<Collection<String>> start;
	private final SetCommand<Collection<String>> stop;
	private final SetCommand<Collection<String>> restart;
	
	/**
	 * The constructor for a class enabling control of and IOC.
	 * 
	 * @param server
	 *             The config server in which the IOC is running.
	 *
	 */
	public IocControl(ConfigServer server) {
		this.iocs = new UpdatedObservableAdapter<>(server.iocStates());
		
		start = server.startIoc();
		stop = server.stopIoc();
		restart = server.restartIoc();
	}
	
	/**
	 * Returns a collection of iocs on the config server.
	 * 
	 * @return
	 *         A collection of iocs on the config server.
	 */
    public UpdatedValue<Collection<IocState>> iocs() {
		return iocs;
	}
	
    /**
     * Returns a command to start an IOC.
     * 
     * @return
     *          A  command to start an IOC.
     */
	public SetCommand<Collection<String>> startIoc() {
		return start;
	}

	/**
     * Returns a command to stop an IOC.
     * 
     * @return
     *          A  command to stop an IOC.
     */
	public SetCommand<Collection<String>> stopIoc() {
		return stop;
	}
	
	/**
     * Returns a command to restart an IOC.
     * 
     * @return
     *          A  command to restart an IOC.
     */
	public SetCommand<Collection<String>> restartIoc() {
		return restart;
	}
}
