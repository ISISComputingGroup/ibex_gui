
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

package uk.ac.stfc.isis.ibex.banner.motion;

import uk.ac.stfc.isis.ibex.epics.adapters.ModelAdapter;
import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * An observable used to control the motors.
 */
public class ObservableMotionControl extends ModelAdapter {

	private static final Long STOP_VALUE = 1L;
	
	private final SameTypeWriter<Long> stop = new SameTypeWriter<Long>();;

	private SettableUpdatedValue<Boolean> canWrite = new SettableUpdatedValue<Boolean>();
	
    /**
     * Constructor for the observable.
     * 
     * @param stop
     *            The writable to write to so as to stop all motion.
     */
	public ObservableMotionControl(Writable<Long> stop) {
		this.stop.writeTo(stop);
		stop.subscribe(new SameTypeWriter<Long>() {
			@Override
			public void onCanWriteChanged(boolean canwrite) {
				ObservableMotionControl.this.canWrite.setValue(canwrite);
			}
		});
	}

    /**
     * Sends a value to the PV to stop all motors.
     */
	public void stop() {
	    stop.uncheckedWrite(STOP_VALUE);
	}

	/**
	 * Whether this writable can be written to at present.
	 * @return an updated value wrapping whether you can write to this writable
	 */
	public UpdatedValue<Boolean> canWrite() {
		return canWrite;
	}
}
