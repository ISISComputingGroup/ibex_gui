
package uk.ac.stfc.isis.ibex.ui.scriptgenerator.dialogs;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.ui.about.AboutDialogBox;

public class ScriptGeneratorAboutHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		var shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		var dialog = new AboutDialogBox(shell, "Script Generator");
		dialog.open();
		return null;
	}

}
