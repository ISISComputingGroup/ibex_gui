
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

import uk.ac.stfc.isis.ibex.banner.InMotionState;
import uk.ac.stfc.isis.ibex.banner.Observables;
import uk.ac.stfc.isis.ibex.banner.motion.ObservableMotionControl;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.banner.controls.ControlModel;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorStateObserver;

public class MotionControlModel extends ModelObject implements ControlModel {
	
	private final ObservableMotionControl motionControl;
	private static final String TEXT = "Stop All";
	
	private IndicatorStateObserver<InMotionState> inMotion;
	
	public MotionControlModel(Observables observables) {
		motionControl = new ObservableMotionControl(observables.stop);
		inMotion = new IndicatorStateObserver<InMotionState>(observables.inMotion, new InMotionViewState());
	}
	
	public UpdatedValue<Boolean> enabled() {	
		return inMotion.bool();
	}
	
	public void click() {
		motionControl.stop();
	}
	
	public String text() {
		return TEXT;
	}
}
