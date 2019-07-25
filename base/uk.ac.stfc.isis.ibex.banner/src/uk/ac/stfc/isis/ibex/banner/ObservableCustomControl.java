
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2019 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.banner;

import uk.ac.stfc.isis.ibex.epics.adapters.ModelAdapter;
import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * An observable used to control the motors.
 */
public class ObservableCustomControl extends ModelAdapter {

    private Long valueToWrite;
    
    private final SameTypeWriter<Long> act = new SameTypeWriter<Long>();

    private SettableUpdatedValue<Boolean> canWrite = new SettableUpdatedValue<Boolean>();
    
    /**
     * Constructor for the observable.
     * 
     * @param act
     *            The writable to write to so as to perform the action.
     * @param valueToWrite
     *            The value to write to the PV to perform the action
     */
    public ObservableCustomControl(Writable<Long> act, Long valueToWrite) {
        this.act.writeTo(act);
        act.subscribe(new SameTypeWriter<Long>() {
            @Override
            public void onCanWriteChanged(boolean canwrite) {
                ObservableCustomControl.this.canWrite.setValue(canwrite);
            }
        });
        this.valueToWrite = valueToWrite;
    }

    /**
     * Sends a value to the PV to stop all motors.
     */
    public void act() {
        act.uncheckedWrite(valueToWrite);
    }

    /**
     * Whether this writable can be written to at present.
     * @return an updated value wrapping whether you can write to this writable
     */
    public UpdatedValue<Boolean> canWrite() {
        return canWrite;
    }
    
    /**
     * @param valueToWrite the value to write to set
     */
    public void setValueToWrite(Long valueToWrite) {
        this.valueToWrite = valueToWrite;
    }
    
    /**
     * @param writable the writable to set
     */
    public void setWritable(Writable<Long> writable) {
        act.writeTo(writable);
    }
}
