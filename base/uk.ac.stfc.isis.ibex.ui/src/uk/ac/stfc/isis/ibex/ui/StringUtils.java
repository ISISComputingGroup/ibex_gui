package uk.ac.stfc.isis.ibex.ui;

/**
 * Utilities for formatting strings.
 */
public class StringUtils {
	
	private static final String ELIPSES = "…";
	
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
