
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

package uk.ac.stfc.isis.ibex.synoptic.model;

import uk.ac.stfc.isis.ibex.configserver.AlarmState;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.epics.writing.Writer;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * A component property that writes its value to a PV.
 */
public class WritableComponentProperty extends ComponentProperty {

	private final SameTypeWriter<String> writer = new SameTypeWriter<>();

	private final ReadableComponentProperty valueSource;
	
	/**
     * @param displayName display name of the component property
     * @param source an observable for the source PV
     * @param sourceAlarm an observable for the source PV's alarm
     * @param destination a writer to the destination PV
     */
	public WritableComponentProperty(
			String displayName, 
			ForwardingObservable<String> source,
			ForwardingObservable<AlarmState> sourceAlarm,
			Writable<String> destination) {
		super(displayName);
		valueSource = new ReadableComponentProperty(displayName, source, sourceAlarm);
		writer.writeTo(destination);	
	}
	
	/**
	 * @return the writer to the destination PV
	 */
	public Writer<String> writer() {
		return writer;
	}
	
	/**
     * @return String an UpdatedValue containing the value
     */
	public UpdatedValue<String> value() {
		return valueSource.value();
	}
	
	/**
	 * @return the ReadableComponentProperty associated with this PV
	 */
	public ReadableComponentProperty sourceReadableProperty() {
	    return valueSource;
	}
}
