
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

package uk.ac.stfc.isis.ibex.ui.banner.indicators;

import org.eclipse.swt.graphics.Color;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class IndicatorStateObserver<T> implements Closable {

    private final Observer<T> sourceObserver = new BaseObserver<T>() {
		@Override
		public void onValue(T value) {
			setState(value);
		}

		@Override
		public void onError(Exception e) {
			setState(null);
		}

		@Override
		public void onConnectionStatus(boolean isConnected) {
			if (!isConnected) {
				setState(null);
			}
		}
	};
	
	protected Subscription sourceSubscription;
	protected final SettableUpdatedValue<String> text;
	protected final SettableUpdatedValue<Color> color;
	protected final SettableUpdatedValue<Boolean> bool;	
	protected final SettableUpdatedValue<Boolean> availability;
	protected final IndicatorViewStateConverter<T> viewConverter;
	
	public IndicatorStateObserver(ForwardingObservable<T> source, IndicatorViewStateConverter<T> viewConverter) {
		this.viewConverter = viewConverter;
		text = new SettableUpdatedValue<>();
		color = new SettableUpdatedValue<>();
		bool = new SettableUpdatedValue<>();
		availability = new SettableUpdatedValue<>();
		sourceSubscription = source.addObserver(sourceObserver);
	}	
	
	public UpdatedValue<String> text() {
		return text;
	}
	
	public UpdatedValue<Color> color() {
		return color;
	}	
	
	public UpdatedValue<Boolean> bool() {
		return bool;
	}	
	
	public UpdatedValue<Boolean> availability() {
		return bool;
	}
	
	@Override
	public void close() {
		sourceSubscription.removeObserver();
	}	
	
	protected void setState(T value) {
		viewConverter.setState(value);
		text.setValue(viewConverter.getMessage());
		color.setValue(viewConverter.color());
		bool.setValue(viewConverter.toBool());
		bool.setValue(viewConverter.availability());
	}
	
}
