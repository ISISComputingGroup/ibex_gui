package uk.ac.stfc.isis.ibex.scriptgenerator;

/**
 * An exception for when trying to generate a file from a generated script.
 */
@SuppressWarnings("serial")
public class FileGeneratorException extends Exception {
	
	public FileGeneratorException(String message) {
		super(message);
	}

}
