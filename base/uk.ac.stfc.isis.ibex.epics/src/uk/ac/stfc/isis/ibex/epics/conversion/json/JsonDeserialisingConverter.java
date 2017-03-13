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

package uk.ac.stfc.isis.ibex.epics.conversion.json;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;

/**
 * Converter for deserialising JSON into an object of type T.
 * 
 * @param <T>
 *            The type to deserialise into.
 */
public class JsonDeserialisingConverter<T> extends Converter<String, T> {

	protected final Gson gson;
	private final Class<T> classOfT;

    /**
     * Instantiates a new json deserialising converter where the gson instance
     * is specified.
     *
     * @param classOfT
     *            the class of the final converted json
     * @param gson
     *            the gson deserialisation library
     */
	public JsonDeserialisingConverter(Class<T> classOfT, Gson gson) {
		this.gson = gson;
		this.classOfT = classOfT;
	}
	
    /**
     * Instantiates a new json deserialising converter using a new
     * deserialisation library.
     *
     * Constructor.
     * 
     * @param outputType
     *            the output type
     */
	public JsonDeserialisingConverter(Class<T> outputType) {
		this(outputType, new Gson());
	}
	
    /**
     * Converts.
     * 
     * @param value
     *            the value
     * @return the parsed value
     * @throws ConversionException
     *             if there was an error
     */
	@Override
	public T convert(String value) throws ConversionException {
		try {
			// NB. Gson uses reflection to initialise the internal
			//     fields of the class: the returned class will not 
			//     have been initialised through its constructor.
			return parseJson(value);
		} catch (JsonSyntaxException e) {
			throw new ConversionException("Error parsing json", e);
		}
	}
	
    /**
     * Parses the json from a string to the class.
     *
     * @param json
     *            the json text to parse
     * @return parsed json as an object of the correct type T
     * @throws JsonSyntaxException
     *             if there is a problem converting the object
     */
    protected T parseJson(String json) {
		return gson.fromJson(json, classOfT);
	}
}
