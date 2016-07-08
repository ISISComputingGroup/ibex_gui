
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

package uk.ac.stfc.isis.ibex.ui.widgets.observable;

import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.epics.writing.TransformingWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;


/**
 * Links an observable and writable object connected to a binary (boolean) PV.
 * Writing is converted to Long as Boolean output is not supported by PVManager.
 */
public class BooleanWritableObservableAdapter implements Closable {
	
    private final UpdatedValue<Boolean> value;
    private final SettableUpdatedValue<Boolean> canSetValue;
    private final TransformingWriter<Boolean, Long> writer = new TransformingWriter<Boolean, Long>() {
        @Override
        public void write(Boolean value) {
            if (transform(value) != writer.lastWritten()) {
                super.write(value);
            }
        }

        @Override
        protected Long transform(Boolean value) {
            if (value) {
                return (long) 1;
            }
            return (long) 0;
        }
	};

	private final Subscription writerSubscription;
	private final Subscription writableSubscription;	
	
	/**
	 * @param writable The object for writing to a PV
	 * @param observable The object for observing a PV
	 */
    public BooleanWritableObservableAdapter(Writable<Long> writable, ForwardingObservable<Boolean> observable) {
        value = new UpdatedObservableAdapter<Boolean>(observable);
        canSetValue = new SettableUpdatedValue<>();
        canSetValue.setValue(writable.canWrite());
		
		writableSubscription = writer.writeTo(writable);
		writerSubscription = writable.subscribe(writer);
	}
	
    /**
     * Returns the current value of the associated PV.
     * 
     * @return the value
     */
    public UpdatedValue<Boolean> value() {
        return value;
	}
	
    /**
     * Writes a new value to the associated PV.
     * 
     * @param value the new value
     */
    public void setValue(Boolean value) {
        writer.write(value);
	}

    /**
     * Returns whether writing value to PV is allowed.
     * 
     * @return the boolean value
     */
    public UpdatedValue<Boolean> canSetValue() {
        return canSetValue;
	}

    /**
     * Removes observers when connection to PV is closed.
     */
	@Override
	public void close() {
		writerSubscription.removeObserver();
		writableSubscription.removeObserver();
	}
}
