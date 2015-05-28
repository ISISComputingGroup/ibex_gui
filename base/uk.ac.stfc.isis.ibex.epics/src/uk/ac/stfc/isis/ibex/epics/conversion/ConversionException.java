package uk.ac.stfc.isis.ibex.epics.conversion;

public class ConversionException extends Exception {

	private static final long serialVersionUID = 1L;

	public ConversionException(String message) {
		super(message);
	}

    public ConversionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
