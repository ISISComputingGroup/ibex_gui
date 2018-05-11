package uk.ac.stfc.isis.ibex.journal.formatters;

/**
 * Formatter that formats a number of seconds into a duration.
 * @author ynq66733
 *
 */
public class DurationJournalFormatter implements IJournalFormatter {
	
	private static final long SECONDS_PER_MINUTE = 60;
	private static final long MINUTES_PER_HOUR = SECONDS_PER_MINUTE;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String format(String input) {
		try {
			Long duration = Long.parseLong(input);
			
			long seconds = duration % SECONDS_PER_MINUTE;
			long minutes = (duration % (SECONDS_PER_MINUTE * MINUTES_PER_HOUR)) / MINUTES_PER_HOUR;
			long hours = duration / (SECONDS_PER_MINUTE * MINUTES_PER_HOUR);

			return String.format("%d:%02d:%02d", hours, minutes, seconds);
		} catch (NumberFormatException e) {
			// If failed to parse, return the number of seconds rather than crashing.
			return input + " s";
		}
	}
}
