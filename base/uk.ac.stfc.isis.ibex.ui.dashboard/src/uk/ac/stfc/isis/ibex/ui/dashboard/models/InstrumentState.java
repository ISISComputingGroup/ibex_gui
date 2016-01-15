
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

package uk.ac.stfc.isis.ibex.ui.dashboard.models;

import org.eclipse.swt.graphics.Color;

import uk.ac.stfc.isis.ibex.dae.DaeRunState;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.dashboard.widgets.RunState;

public class InstrumentState implements Closable {
	
    private final Observer<DaeRunState> sourceObserver = new BaseObserver<DaeRunState>() {
		@Override
		public void onValue(DaeRunState value) {
			setState(runState(value));
		}

		@Override
		public void onError(Exception e) {
			setState(RunState.UNKNOWN);
		}

		@Override
		public void onConnectionStatus(boolean isConnected) {
			if (!isConnected) {
				setState(RunState.UNKNOWN);
			}
		}
	};
	
	private final Subscription sourceSubscription;
	private final SettableUpdatedValue<String> text;
	private final SettableUpdatedValue<Color> color;
	
	public InstrumentState(ForwardingObservable<DaeRunState> source) {
		text = new SettableUpdatedValue<>();
		color = new SettableUpdatedValue<>();
		sourceSubscription = source.addObserver(sourceObserver);
	}

	public UpdatedValue<String> text() {
		return text;
	}
	
	public UpdatedValue<Color> color() {
		return color;
	}
	
	@Override
	public void close() {
		sourceSubscription.removeObserver();
	}
	
	private void setState(RunState state) {
		text.setValue(state.name());
		color.setValue(state.color());
	}
	
	private static RunState runState(DaeRunState state) {
    	for (RunState runState : RunState.values()) {
    		if (runState.name().equalsIgnoreCase(state.name())) {
    			return runState;
    		}
    	}
    	
        return RunState.UNKNOWN;
	}
}
