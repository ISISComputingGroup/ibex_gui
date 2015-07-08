
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

package uk.ac.stfc.isis.ibex.epics.adapters;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialisableObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;

public class UpdatedObservableAdapter<T> extends SettableUpdatedValue<T> implements Closable {

	private Subscription subscription;
	
	private final InitialisableObserver<T> observer = new BaseObserver<T>() {
		@Override
		public void onValue(T value) {
			setValue(value);
		}

		@Override
		public void onError(Exception e) {
			error(e);
		}

		@Override
		public void onConnectionChanged(boolean isConnected) {
			connectionChanged(isConnected);
		}
	};
	
	public UpdatedObservableAdapter(InitialiseOnSubscribeObservable<T> observable) {
		subscribeTo(observable);
	}
		
	protected void error(Exception e) {
	};
	
	protected void connectionChanged(boolean isConnected) {
		if (!isConnected) {
			setValue(null);
		}
	};
	
	@Override
	public void close() {
		subscription.cancel();
	}
	
	private void subscribeTo(InitialiseOnSubscribeObservable<T> observable) {
		subscription = observable.subscribe(observer);
	}
}
