package uk.ac.stfc.isis.ibex.ui.configserver.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.model.Awaited;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.EditConfigDialog;

public class EditCurrentConfigHandler extends ConfigHandler<Configuration> {
	
	private static final String TITLE = "Edit configuration";
	private static final String SUB_TITLE = "Editing the current configuration";
	
	private EditConfigDialog dialog;
	
	public EditCurrentConfigHandler() {
		super(SERVER.setCurrentConfig());
	}

	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {		
		UpdatedValue<EditableConfiguration> config = new UpdatedObservableAdapter<>(EDITING.currentConfig());
		
		if (Awaited.returnedValue(config, 1)) {
			openDialog(config.getValue());			
		}
				
		return null;
	}
	
	private void openDialog(EditableConfiguration config) {
		dialog = new EditConfigDialog(shell(), TITLE, SUB_TITLE, config, false, false);	
		if (dialog.open() == Window.OK) {
			if (dialog.doAsComponent()) {
				SERVER.saveAsComponent().write(dialog.getComponent());
			} else {
				SERVER.setCurrentConfig().write(dialog.getConfig());
				SERVER.save().write(dialog.getConfig().name());
			}
		}
	}
}
