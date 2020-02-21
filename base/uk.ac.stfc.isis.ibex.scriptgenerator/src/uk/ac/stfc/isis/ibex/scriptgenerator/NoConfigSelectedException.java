package uk.ac.stfc.isis.ibex.scriptgenerator;

/**
 * An exception for when we are trying to refer to the selected config when
 *  there is no config selected e.g. when there is no config to select.
 * 
 * @author James King
 *
 */
@SuppressWarnings("serial")
public class NoConfigSelectedException extends Exception {
	
	public NoConfigSelectedException(String message) {
		super(message);
	}

}
