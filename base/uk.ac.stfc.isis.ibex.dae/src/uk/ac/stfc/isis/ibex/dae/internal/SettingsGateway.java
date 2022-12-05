
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

package uk.ac.stfc.isis.ibex.dae.internal;

import java.io.IOException;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

/**
 * A gateway through which all DAE settings changes are sent.
 */
public abstract class SettingsGateway implements Closable {
    private Observer<String> settingsObserver = new BaseObserver<String>() {
		@Override
		public void onValue(String value) {
			setFromText(value);
		}	
	};
	
	private final Subscription sourceSubscription;

	private final Writable<String> settingsDestination;

	/**
	 * Initialise the gateway.
	 * @param settingsSource the source PV to use for getting the settings
	 * @param settingsDestination the destination PV to use for writing the settings
	 */
	public SettingsGateway(ForwardingObservable<String> settingsSource, Writable<String> settingsDestination) {
		sourceSubscription = settingsSource.subscribe(settingsObserver);
		this.settingsDestination = settingsDestination;
	}
	
	/**
	 * Sends the update.
	 * 
	 * @throws IOException if the send failed
	 */
	public synchronized void sendUpdate() throws IOException {
		settingsDestination.write(asText());
	}

	/**
	 * Get the settings as text.
	 * @return the settings
	 */
	protected abstract String asText();
	
	/**
	 * Set the settings from text.
	 * @param text the text
	 */
	protected abstract void setFromText(String text);
	
	@Override
	public void close() {
		sourceSubscription.cancelSubscription();
	}
}
