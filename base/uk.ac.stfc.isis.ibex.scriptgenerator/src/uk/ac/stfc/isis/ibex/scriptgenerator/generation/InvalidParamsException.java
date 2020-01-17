package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

@SuppressWarnings("serial")
public class InvalidParamsException extends Exception {
	
	public InvalidParamsException(String errorMessage) {
		super(errorMessage);
	}

}
