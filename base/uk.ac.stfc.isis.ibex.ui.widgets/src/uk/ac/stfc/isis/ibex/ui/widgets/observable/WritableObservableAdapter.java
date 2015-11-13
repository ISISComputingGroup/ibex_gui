
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

import uk.ac.stfc.isis.ibex.epics.adapters.TextUpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.observing.BaseCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.epics.writing.BaseWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;


/**
 * Links a read PV and a writable PV.
 *
 */
public class WritableObservableAdapter implements Closable {
	
	private final UpdatedValue<String> text;
	private final SettableUpdatedValue<Boolean> canSetText;
	private final BaseWriter<String, String> writer = new BaseWriter<String, String>() {
		@Override
		public void write(String value) {
			writeToWritables(value);
		}
		
		public void onCanWriteChanged(boolean canWrite) {
			canSetText.setValue(canWrite);
		}
	};

	private final Subscription writerSubscription;
	private final Subscription writableSubscription;	
	
	/**
	 * @param writable The object for writing to a PV
	 * @param observable The object for observing a PV
	 */
	public WritableObservableAdapter(Writable<String> writable, BaseCachingObservable<String> observable) {
		text = new TextUpdatedObservableAdapter(observable);
		canSetText = new SettableUpdatedValue<>();
		canSetText.setValue(writable.canWrite());
		
		writableSubscription = writer.writeTo(writable);
		writerSubscription = writable.subscribe(writer);
	}
	
	public UpdatedValue<String> text() {
		return text;
	}
	
	public void setText(String text) {
		if (text != writer.lastWritten()) {
			writer.write(text);
		}
	}
	
	public UpdatedValue<Boolean> canSetText() {
		return canSetText;
	}

	@Override
	public void close() {
		writerSubscription.removeObserver();
		writableSubscription.removeObserver();
	}
}
