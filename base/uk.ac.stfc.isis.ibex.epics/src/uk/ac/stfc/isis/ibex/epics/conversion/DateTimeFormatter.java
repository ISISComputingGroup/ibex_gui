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
