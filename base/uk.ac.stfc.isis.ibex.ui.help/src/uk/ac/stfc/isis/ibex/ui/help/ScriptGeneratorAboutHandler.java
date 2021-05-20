
package uk.ac.stfc.isis.ibex.ui.help;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

public class ScriptGeneratorAboutHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		var shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		AboutDialogBox dialog = new AboutDialogBox(shell);
		dialog.open();
		return null;
	}

}
