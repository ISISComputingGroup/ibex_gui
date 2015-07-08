
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.TransformingObservable;
import uk.ac.stfc.isis.ibex.experimentdetails.Parameter;

public class ParametersObservable extends TransformingObservable<Collection<String>, Collection<Parameter>> {

	private final ExperimentDetailsVariables variables;

	public ParametersObservable(
			ExperimentDetailsVariables variables, 
			ClosableCachingObservable<Collection<String>> availableParameters) {
		this.variables = variables;
		setSource(availableParameters);
	}
	
	@Override
	protected Collection<Parameter> transform(Collection<String> value) {
		List<Parameter> parameters = new ArrayList<>();
		for (String address : value) {
			parameters.add(createParameter(address));
		}
		
		return parameters;
	}
	
	protected Parameter createParameter(String address) {
		return new ObservableParameter(
				variables.parameterName(address),
				variables.parameterUnits(address),
				variables.parameterValue(address),
				variables.parameterValueSetter(address));
	}
}
