
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

package uk.ac.stfc.isis.ibex.epics.conversion;


/**
 * Abstract base class for converters.
 *
 * @param <A>
 *            The type to convert from.
 * @param <B>
 *            The type to convert to.
 */
public abstract class Converter<A, B> {
	
    /**
     * Apply one converter to another to create a converter with multiple stages.
     * Apply a converter from B to C to a converted form A to B to get a converter from A to C.
     * 
     * @param <C> the type to convert to
     * @param converter a converter from B to C
     * @return a converter from A to C
     */
	public <C> Converter<A, C> apply(final Converter<B, C> converter) {
		final Converter<A, B> self = this;
		return new Converter<A, C>() {
			@Override
            public C convert(A value) throws ConversionException {
				return converter.convert(self.convert(value));
			}
		};
	}
	
	/**
	 * Convert A into B.
	 * 
	 * @param value the value to convert
	 * @return the converted value
	 * @throws ConversionException if a conversion error occurs
	 */
	public abstract B convert(A value) throws ConversionException;
}
   
