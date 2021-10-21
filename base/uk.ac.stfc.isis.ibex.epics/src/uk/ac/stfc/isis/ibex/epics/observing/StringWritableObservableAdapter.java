
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

package uk.ac.stfc.isis.ibex.epics.observing;

import java.io.IOException;

import uk.ac.stfc.isis.ibex.epics.adapters.TextUpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.epics.writing.OnCanWriteChangeListener;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;


/**
 * Links an observable and writable object connected to a (StringChannel) PV.
 *
 */
public class StringWritableObservableAdapter implements Closable {
	
	private final UpdatedValue<String> text;
	private SettableUpdatedValue<Boolean> canSetText;
	private final OnCanWriteChangeListener canWriteListener = canWrite -> canSetText.setValue(canWrite);
	private String lastValueWritten = null;
	private final Writable<String> writable;	
	
	/**
	 * @param writable The object for writing to a PV
	 * @param observable The object for observing a PV
	 */
    public StringWritableObservableAdapter(Writable<String> writable, ClosableObservable<String> observable) {
		text = new TextUpdatedObservableAdapter(observable);
		canSetText = new SettableUpdatedValue<>();
		canSetText.setValue(writable.canWrite());
		
		this.writable = writable;
		writable.addOnCanWriteChangeListener(canWriteListener);
	}

    /**
     * Returns the current string value of the associated PV.
     * 
     * @return the string value
     */
	public UpdatedValue<String> text() {
		return text;
	}
	
    /**
     * Writes a new string value to the associated PV.
     * 
     * @param text
     *            the new value
     * @throws IOException 
     */
	public void setText(String text) throws IOException {
		if (!text.equals(lastValueWritten)) {
			writable.write(text);
			lastValueWritten = text;
		}
	}
	
	/**
     * Writes a new string value to the associated PV.
     * 
     * @param text
     *            the new value
     */
	public void uncheckedSetText(String text) {
        if (!text.equals(lastValueWritten)) {
            writable.uncheckedWrite(text);
            lastValueWritten = text;
        }
    }

    /**
     * Returns whether writing value to PV is allowed.
     * 
     * @return the boolean value
     */
	public UpdatedValue<Boolean> canSetText() {
		return canSetText;
	}

    /**
     * Removes observers when connection to PV is closed.
     */
	@Override
	public void close() {
	    writable.removeOnCanWriteChangeListener(canWriteListener);
	}
}
