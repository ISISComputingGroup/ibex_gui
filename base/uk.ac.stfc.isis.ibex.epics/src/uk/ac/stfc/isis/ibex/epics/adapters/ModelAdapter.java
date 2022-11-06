
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


package uk.ac.stfc.isis.ibex.epics.adapters;

import java.util.function.Function;

import uk.ac.stfc.isis.ibex.epics.observing.ConvertingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * An adapter from Observables to UpdatedValues.
 */
public class ModelAdapter extends Closer {
	
	/**
	 * Converts a forwarding observable of type T to one of type String.
	 * @param <T> the type of the underlying value
	 * @param observable the underlying observable
	 * @param converter the converter
	 * @return the converted observable
	 */
	protected <T> ForwardingObservable<String> convert(ForwardingObservable<T> observable, Function<T, String> converter) {
		ConvertingObservable<T, String> converted = new ConvertingObservable<>(observable, converter);
		return registerForClose(new ForwardingObservable<>(converted));
	}
	
	/**
	 * Adapt an observable to an updated value.
	 * @param <T> the type of value
	 * @param observable the observable
	 * @return the updated value
	 */
	protected <T> UpdatedValue<T> adapt(ForwardingObservable<T> observable) {
		UpdatedObservableAdapter<T> adapted = new UpdatedObservableAdapter<>(observable);
		return registerForClose(adapted);
	}
}
