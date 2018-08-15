
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

import uk.ac.stfc.isis.ibex.banner.Observables;
import uk.ac.stfc.isis.ibex.banner.motion.ObservableMotionControl;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.banner.controls.ControlModel;

/**
 * The model for the stop all motors button in the banner.
 */
public class MotionControlModel extends ModelObject implements ControlModel {
	
	private final ObservableMotionControl motionControl;
	private static final String TEXT = "Stop All";
	
	/**
	 * The model for the stop all motors button in the banner.
	 * 
	 * @param observables
	 *                     The observables class associated with the motion control.
	 */
	public MotionControlModel(Observables observables) {
		motionControl = new ObservableMotionControl(observables.stop);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UpdatedValue<Boolean> enabled() {	
		return motionControl.canWrite();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void click() {
		motionControl.stop();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String text() {
		return TEXT;
	}
}
