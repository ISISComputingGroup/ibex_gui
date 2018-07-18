package uk.ac.stfc.isis.ibex.ui.scripting;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.python.pydev.shared_interactive_console.console.ui.ScriptConsole;

/**
 * Interrupt the currently active script. 
 */
public class InterruptScript extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ScriptConsole.getActiveScriptConsole().interrupt();
		return null;
	}

}
