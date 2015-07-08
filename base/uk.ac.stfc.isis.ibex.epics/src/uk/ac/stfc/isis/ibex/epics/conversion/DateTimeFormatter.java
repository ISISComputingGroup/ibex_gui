
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

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatterBuilder;

public class DateTimeFormatter extends Converter<String, String> {
		
	private static final String ISO_PATTERN_NO_MILLIS_NO_OFFSET =  "yyyy-MM-dd'T'HH:mm:ss";
	private static final String DATE_TIME_MILLIS =  "yyyy/MM/dd HH:mm:ss.SSS";
	private static final String US_DATE_TIME =  "MM/dd/yyyy HH:mm:ss";

	private static final String OUTPUT_DATE_TIME =  "dd/MM/yyyy HH:mm:ss";

	
	private static final org.joda.time.format.DateTimeParser[] PARSERS = {
		DateTimeFormat.forPattern(ISO_PATTERN_NO_MILLIS_NO_OFFSET).getParser(),
		DateTimeFormat.forPattern(DATE_TIME_MILLIS).getParser(),
		DateTimeFormat.forPattern(US_DATE_TIME).getParser(),
	};
	
	private static final org.joda.time.format.DateTimeFormatter PARSER = 
			new DateTimeFormatterBuilder()
				.append(null, PARSERS)
				.toFormatter();
	
	private static final org.joda.time.format.DateTimeFormatter PRINTER = 
			new DateTimeFormatterBuilder()
				.appendPattern(OUTPUT_DATE_TIME)
				.toFormatter()
				.withLocale(Locale.ENGLISH);
	
	@Override
	public String convert(String dateTime) throws ConversionException {
		try {
			DateTime dt = PARSER.parseDateTime(dateTime);
			return PRINTER.print(dt);
		} catch (IllegalArgumentException e) {
			// Not parse-able, so return the original
			return dateTime;
		}		
	}
}
