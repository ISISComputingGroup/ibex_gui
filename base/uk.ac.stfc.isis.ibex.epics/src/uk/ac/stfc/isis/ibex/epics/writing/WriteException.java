package uk.ac.stfc.isis.ibex.epics.writing;

public class WriteException extends Exception {

	private static final long serialVersionUID = 1L;

	public WriteException(){
	}
	
	public WriteException(String message) {
		super(message);
	}
	
	public WriteException(Exception e) {
		super(e);
	}
}
