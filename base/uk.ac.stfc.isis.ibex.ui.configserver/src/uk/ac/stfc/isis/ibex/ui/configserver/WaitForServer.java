
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

package uk.ac.stfc.isis.ibex.ui.configserver;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.ServerStatus;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.Waiting;
import uk.ac.stfc.isis.ibex.ui.WaitingInfo;

public class WaitForServer extends Waiting {

	private final UpdatedValue<ServerStatus> serverStatus;
	
	private boolean isWaiting;
	private WaitingInfo details = new WaitingInfo("");
	
	public WaitForServer() {
		serverStatus = new UpdatedObservableAdapter<>(Configurations.getInstance().server().serverStatus());
		
		serverStatus.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ServerStatus status = serverStatus.getValue();
				if (status != null) {
					setIsWaiting(status);
					setDetails(status);
				}
			}
		}, true);
	}

	@Override
	public boolean isWaiting() {
		return isWaiting;
	}

	@Override
	public WaitingInfo getDetails() {
		return details;
	}
	
	private void setIsWaiting(ServerStatus status) {
		firePropertyChange("isWaiting", this.isWaiting, this.isWaiting = status.isBusy());
	}
	
	private void setDetails(ServerStatus status) {		
		firePropertyChange("details", this.details, this.details = new WaitingInfo(composeReason(status)));
	}

	private String composeReason(ServerStatus status) {
		return status.isBusy() ? "Waiting" : "Ready";
	}
}
