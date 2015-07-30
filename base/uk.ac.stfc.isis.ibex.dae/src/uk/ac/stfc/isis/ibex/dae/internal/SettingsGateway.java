
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

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialisableObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.epics.writing.BaseWriter;
import uk.ac.stfc.isis.ibex.epics.writing.ConfigurableWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

public abstract class SettingsGateway implements Closable {
	private InitialisableObserver<String> settingsObserver = new BaseObserver<String>() {
		@Override
		public void onValue(String value) {
			setFromText(value);
		}

		@Override
		public void onError(Exception e) {
			
		}

		@Override
		public void onConnectionChanged(boolean isConnected) {
			
		}	
	};
	
	private final Subscription sourceSubscription;
	private final Subscription destinationSubscription;
	private final Subscription writerSubscription;

	private ConfigurableWriter<String, String> updateWriter = new BaseWriter<String, String>() {
		@Override
		public void write(String value) {
			writeToWritables(value);
		}
	};

	public SettingsGateway(InitialiseOnSubscribeObservable<String> settingsSource, Writable<String> settingsDestination) {
		sourceSubscription = settingsSource.addObserver(settingsObserver);
		destinationSubscription = settingsDestination.subscribe(updateWriter);
		writerSubscription = updateWriter.writeTo(settingsDestination);
	}
	
	public void sendUpdate() {
		updateWriter.write(asText());
	}

	protected abstract String asText();
	
	protected abstract void setFromText(String text);
	
	@Override
	public void close() {
		sourceSubscription.cancel();
		destinationSubscription.cancel();
		writerSubscription.cancel();
	}
}
