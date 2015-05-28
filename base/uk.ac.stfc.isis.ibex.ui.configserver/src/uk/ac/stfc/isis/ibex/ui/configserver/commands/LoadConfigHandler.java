package uk.ac.stfc.isis.ibex.ui.configserver.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;

import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.ConfigSelectionDialog;

public class LoadConfigHandler extends ConfigHandler<String> {
			
	public LoadConfigHandler() {
		super(SERVER.load());
	}	
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {		
		ConfigSelectionDialog dialog = new ConfigSelectionDialog(shell(), "Load Configuration", SERVER.configsInfo().value(), false);
		if (dialog.open() == Window.OK) {
			configService.write(dialog.selectedConfig());
		}
		
		return null;
	}
}
