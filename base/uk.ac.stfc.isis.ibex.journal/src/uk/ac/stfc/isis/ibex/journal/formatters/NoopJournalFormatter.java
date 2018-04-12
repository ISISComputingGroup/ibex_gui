package uk.ac.stfc.isis.ibex.journal.formatters;

/**
 * No-op formatter that just returns the input data.
 */
public class NoopJournalFormatter implements IJournalFormatter {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String format(String input) {
		return input;
	}
}
