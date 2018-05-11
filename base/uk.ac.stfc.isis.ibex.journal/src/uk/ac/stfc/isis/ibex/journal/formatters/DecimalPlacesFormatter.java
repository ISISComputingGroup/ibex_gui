package uk.ac.stfc.isis.ibex.journal.formatters;

/**
 * Formatter that formats a double to a specified number of decimal places.
 */
public class DecimalPlacesFormatter implements IJournalFormatter {
	
	private final int decimalPlaces;
	
	/**
	 * Create a new Decimal places formatter which will format the output to have the specified number of decimal places.
	 * @param decimalPlaces the number of decimal places to display.
	 */
	public DecimalPlacesFormatter(int decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String format(String input) {
		try {
			Double duration = Double.parseDouble(input);
			return String.format("%." + decimalPlaces + "f", duration);
		} catch (NumberFormatException e) {
			// If failed to parse, return input rather than crashing.
			return input;
		}
	}
}
