// Interface to allow error messages to be set on a dialog, without passing the dialog around
package uk.ac.stfc.isis.ibex.ui.configserver.dialogs;

public interface MessageDisplayer {
	public void setErrorMessage(String source, String message);
}
