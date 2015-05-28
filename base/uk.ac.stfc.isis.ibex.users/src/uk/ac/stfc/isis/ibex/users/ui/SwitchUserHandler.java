package uk.ac.stfc.isis.ibex.users.ui;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;

public class SwitchUserHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Dialog dialog = new SwitchUserDialog(Display.getCurrent().getActiveShell());
		dialog.setBlockOnOpen(true);
		
		dialog.open();
		
		return null;
	}

}
