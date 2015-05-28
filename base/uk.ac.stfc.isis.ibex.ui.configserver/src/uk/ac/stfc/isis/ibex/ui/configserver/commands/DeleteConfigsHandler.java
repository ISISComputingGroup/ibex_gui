package uk.ac.stfc.isis.ibex.ui.configserver.commands;

import java.util.Collection;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;

import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MultipleConfigsSelectionDialog;

public class DeleteConfigsHandler extends ConfigHandler<Collection<String>> {
		
	public DeleteConfigsHandler() {
		super(SERVER.deleteConfigs());
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {		
		MultipleConfigsSelectionDialog dialog = new MultipleConfigsSelectionDialog(shell(), "Delete Configurations", SERVER.configsInfo().value(), false);
		if (dialog.open() == Window.OK) {
			configService.write((dialog.selectedConfigs()));
		}
		
		return null;
	}
}
