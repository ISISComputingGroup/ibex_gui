package uk.ac.stfc.isis.ibex.ui;

/**
 * Utilities for formatting strings.
 */
public final class StringUtils {
	
	private static final String ELIPSES = "…";
	
	/**
	 * Private constructor for utility class.
	 */
	private StringUtils() { }
	
	/**
	 * If a string is too long, truncate it and add an ellipsis to the end.
	 * 
	 * @param in the input string
	 * @param length the maximum length
	 * @return the string, truncated and with ellipses added beyond length
	 */
	public static String truncateWithEllipsis(String in, int length) {
		String out;
		if (in.length() > length) {
    		out = in.substring(0, length - ELIPSES.length()) + ELIPSES;
    	} else {
    		out = in;
    	}
		return out;
	}
}
