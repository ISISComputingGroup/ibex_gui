package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

/**
 * Exception thrown when the language requested is not supported by the script generator.
 * 
 */
@SuppressWarnings("serial")
public class UnsupportedLanguageException extends Exception {
	
    /**
     * Constructor.
     * @param errorMessage error message for log 
     */
	public UnsupportedLanguageException(String errorMessage) {
		super(errorMessage);
	}

}