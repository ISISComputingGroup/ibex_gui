
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

import java.io.Closeable;

import uk.ac.stfc.isis.ibex.epics.adapters.ModelAdapter;
import uk.ac.stfc.isis.ibex.epics.writing.OnCanWriteChangeListener;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * An observable used to control custom behaviour such as motors.
 */
public class ObservableCustomControl extends ModelAdapter implements Closeable {

    private Long valueToWrite;
    
    private final Writable<Long> act;
    private final OnCanWriteChangeListener canWriteListener = canWrite -> ObservableCustomControl.this.canWrite.setValue(canWrite);

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
        this.act = act;
        act.addOnCanWriteChangeListener(canWriteListener);
        this.valueToWrite = valueToWrite;
    }

    /**
     * Sends a custom value to a custom PV to execute the custom action.
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

    @Override
    public void close() {
        act.removeOnCanWriteChangeListener(canWriteListener);
    }
    
}
