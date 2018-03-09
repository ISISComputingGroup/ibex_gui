package uk.ac.stfc.isis.ibex.journal.formatters;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Formatter to convert a date in SQL form to our own representation.
 */
public class DateTimeJournalFormatter implements IJournalFormatter {
	
	private static final SimpleDateFormat SQL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
	private static final SimpleDateFormat OUTPUT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	@Override
	public String format(String input) {
		try {
			return OUTPUT_DATE_FORMAT.format(SQL_DATE_FORMAT.parse(input));
		} catch (RuntimeException | ParseException e) {
			// If failed to parse, return the input date representation rather than crashing.
			return input;
		}
	}
}
