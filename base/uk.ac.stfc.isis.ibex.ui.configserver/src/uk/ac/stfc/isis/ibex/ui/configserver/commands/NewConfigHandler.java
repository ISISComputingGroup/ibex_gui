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

public class NewConfigHandler extends ConfigHandler<Configuration> {

	private static final String TITLE = "New configuration";
	private static final String SUB_TITLE = "Editing a new configuration";

	public NewConfigHandler() {
		super(SERVER.saveAs());
	}

	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {		
		UpdatedValue<EditableConfiguration> config = new UpdatedObservableAdapter<>(EDITING.blankConfig());

		if (Awaited.returnedValue(config, 1)) {
			openDialog(config.getValue());			
		};
		
		return null;
	}
	
	private void openDialog(EditableConfiguration config) {
		EditConfigDialog editDialog = new EditConfigDialog(shell(), TITLE, SUB_TITLE, config, false, true);	
		if (editDialog.open() == Window.OK) {
			if (editDialog.doAsComponent()) {
				SERVER.saveAsComponent().write(editDialog.getComponent());
			}
			else {
				SERVER.saveAs().write(editDialog.getConfig());
			}
		}
	}
}
