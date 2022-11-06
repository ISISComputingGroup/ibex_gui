
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

package uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels;

import java.io.IOException;

import uk.ac.stfc.isis.ibex.dae.internal.SettingsGateway;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

/**
 * An observable for DAE time-channel settings.
 */
public class ObservingTimeChannels extends XmlBackedTimeChannels implements Closable {
	private final SettingsGateway gateway;

	/**
	 * Creates the observable.
	 * @param settingsSource the source for reading the settings
	 * @param settingsDestination the destination for writing the settings
	 */
	public ObservingTimeChannels(ForwardingObservable<String> settingsSource, Writable<String> settingsDestination) {

		gateway = new SettingsGateway(settingsSource, settingsDestination) {	
			@Override
			protected void setFromText(String text) {
				setXml(text);
			}
			
			@Override
			protected String asText() {
				return xml();
			}
		};
	}
	
	/**
	 * Sends the update via the settings gateway.
	 * @see SettingsGateway.sendUpdate
	 * @throws IOException if the send failed
	 */
	public void sendUpdate() throws IOException {
		gateway.sendUpdate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		gateway.close();
	}
}
