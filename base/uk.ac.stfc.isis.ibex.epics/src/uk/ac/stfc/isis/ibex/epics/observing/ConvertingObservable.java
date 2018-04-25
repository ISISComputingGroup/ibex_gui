
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

import java.util.logging.Logger;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;

/**
 * Converts the source data to a specific type via a converter.
 *
 * @param <R> The type of the first value being observed.
 * @param <T> The required type to transform to.
 */
public class ConvertingObservable<R, T> extends TransformingObservable<R, T> {
	
	private static final Logger LOG = Logger.getLogger(ConvertingObservable.class.getName());

	/**
	 * Contains the transformation function to convert the raw source value to the new observable value.
	 */
	private final Converter<R, T> formatter;
	
	/**
	 * @param source The source of raw data
	 * @param formatter Converts raw data from the source to the value supplied by this observable
	 */
	public ConvertingObservable(ClosableObservable<R> source, Converter<R, T> formatter) {
		this.formatter = formatter;
		// setValue can be set any time from this call so the formatter must be set first.
		setSource(source);
	}
	
	@Override
	protected T transform(R value) {
		T newValue = null;
		
		// Synchronize with the observable source so that further updates are held whilst
		// the data is being processed. Particularly important for large transforms
		// like synoptics and configs.
		
		synchronized(this.source) {
			if (formatter != null && value != null) {
				try {
					newValue = formatter.convert(value);
				} catch (ConversionException e) {
					setError(e);
				}
			} else if (value==null) {
				LOG.warning("Null Value discarded in converting observable: " + toString());
			}
		}
		return newValue;
	}
}
