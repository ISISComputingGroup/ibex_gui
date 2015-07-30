
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

package uk.ac.stfc.isis.ibex.ui.banner.models;

import org.eclipse.swt.graphics.Color;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialisableObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorColours;

public class BatonUserObserver implements Closable {
	private Subscription subscription;
	
	private final InitialisableObserver<String> observer = new BaseObserver<String>() {
		@Override
		public void onValue(String value) {
			checkSelf(value);
		}

		@Override
		public void onError(Exception e) {
			setUnknown();
		}

		@Override
		public void onConnectionStatus(boolean isConnected) {
			if (!isConnected) {
				setUnknown();
			}
		}
	};	
	
	private static final String UNKNOWN = "UNKNOWN";
	private static final String NONE = "NONE";
	private final String SELF;
	
	protected final SettableUpdatedValue<String> text;
	protected final SettableUpdatedValue<Color> color;
	protected final SettableUpdatedValue<Boolean> availability;
	
	public BatonUserObserver(
			InitialiseOnSubscribeObservable<String> observable, String self) {
		text = new SettableUpdatedValue<>();
		color = new SettableUpdatedValue<>();
		availability = new SettableUpdatedValue<>();
		
		subscription = observable.addObserver(observer);
		SELF = self;
	}
	
	@Override
	public void close() {
		subscription.cancel();
	}
	
	private void checkSelf(String value) {
		String user = value.isEmpty() ? NONE : value;
		
		text.setValue("Current user: " +  user);
		
		if (value.equals(SELF)) {
			color.setValue(IndicatorColours.GREEN);
		} else {
			color.setValue(IndicatorColours.BLACK);
		}
	}
	
	private void setUnknown() {
		text.setValue("Current user: " + UNKNOWN);
		color.setValue(IndicatorColours.RED);
	}
	
	public UpdatedValue<String> text() {
		return text;
	}

	public UpdatedValue<Color> color() {
		return color;
	}

	public UpdatedValue<Boolean> availability() {
		return availability;
	}
	
}
