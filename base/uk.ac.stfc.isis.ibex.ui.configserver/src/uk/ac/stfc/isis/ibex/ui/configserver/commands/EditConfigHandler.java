package uk.ac.stfc.isis.ibex.ui.configserver.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.model.Awaited;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.ConfigSelectionDialog;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.EditConfigDialog;

public class EditConfigHandler extends ConfigHandler<Configuration> {

	private static final String TITLE = "Edit configuration";

	public EditConfigHandler() {
		super(SERVER.saveAs());
	}

		
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {		
		ConfigSelectionDialog selectionDialog = new ConfigSelectionDialog(shell(), TITLE, SERVER.configsInfo().value(), false);
		if (selectionDialog.open() == Window.OK) {
			String configName = selectionDialog.selectedConfig();
			edit(configName);
		}
		
		return null;
	}
	
	private void edit(String configName) {
		String subTitle = "Editing " + configName; 
		
		UpdatedValue<EditableConfiguration> config = new UpdatedObservableAdapter<>(EDITING.config(configName));
		if (Awaited.returnedValue(config, 1)) {
			openDialog(subTitle, config.getValue());
		}
	}
	
	private void openDialog(String subTitle, EditableConfiguration config) {
		EditConfigDialog editDialog = new EditConfigDialog(shell(), TITLE, subTitle, config, false, false);	
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
