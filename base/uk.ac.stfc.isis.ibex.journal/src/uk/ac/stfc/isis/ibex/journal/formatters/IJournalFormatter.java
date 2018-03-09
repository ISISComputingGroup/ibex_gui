package uk.ac.stfc.isis.ibex.journal.formatters;

/**
 * Interface for formatters which convert a string in the SQL database to a string that is displayed in the journal table.
 */
public interface IJournalFormatter {
	/**
	 * Formats a string from SQL into it's journal viewer representation.
	 * 
	 * Should not throw any exceptions.
	 * 
	 * @param input the input data from MySQL
	 * @return the string as displayed in the journal view
	 */
	public String format(String input);
}
