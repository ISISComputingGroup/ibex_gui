
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

package uk.ac.stfc.isis.ibex.experimentdetails.internal;

import java.io.IOException;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.experimentdetails.Parameter;

/**
 * Brings together all the observables for the parts of a parameter to hold in one Parameter object.
 */
public class ObservableParameter extends Parameter {
	
	private final BaseObserver<String> nameObserver = new ParameterFieldObserver() {
		@Override
		protected void updateField(String value) {
			setName(value);			
		}
		@Override
		protected String defaultValue() {
			return "Unknown";
		};
	};
	
	private final BaseObserver<String> unitsObserver = new ParameterFieldObserver() {
		@Override
		protected void updateField(String value) {
			setUnits(value);			
		}
	};
	
	private final BaseObserver<String> valueObserver = new ParameterFieldObserver() {
		@Override
		protected void updateField(String value) {
			ObservableParameter.super.setValue(value);			
		}
	};

	private final Writable<String> valueSetter;
	
	public ObservableParameter(
			ForwardingObservable<String> name,
			ForwardingObservable<String> units,
			ForwardingObservable<String> value,
			Writable<String> valueSetter) {

		this.valueSetter = valueSetter;

		name.addObserver(nameObserver);
		units.addObserver(unitsObserver);
		value.addObserver(valueObserver);
	}
	
	@Override
	public void setValue(String value) {
	    try {
	        valueSetter.write(value);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
