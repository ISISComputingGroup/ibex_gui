package uk.ac.stfc.isis.ibex.ui.configserver.commands;

import java.util.Collection;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;

import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MultipleConfigsSelectionDialog;

public class DeleteComponentsHandler extends ConfigHandler<Collection<String>> {
	
	public DeleteComponentsHandler() {
		super(SERVER.deleteComponents());
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {		
		MultipleConfigsSelectionDialog dialog = new MultipleConfigsSelectionDialog(shell(), "Delete Components", SERVER.componentsInfo().value(), true);
		if (dialog.open() == Window.OK) {
			configService.write(dialog.selectedConfigs());
		}
		
		return null;
	}
}
