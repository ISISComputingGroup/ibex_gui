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

public class EditComponentHandler extends ConfigHandler<Configuration> {
	private static final String TITLE = "Edit component";
	
	public EditComponentHandler() {
		super(SERVER.saveAsComponent());
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ConfigSelectionDialog selectionDialog = new ConfigSelectionDialog(shell(), TITLE, SERVER.componentsInfo().value(), true);
		if (selectionDialog.open() == Window.OK) {
			String componentName = selectionDialog.selectedConfig();
			edit(componentName);
		}
		
		return null;
	}
		
	private void edit(String componentName) {
		// TODO
		String subTitle = "Editing " + componentName; 
		
		UpdatedValue<EditableConfiguration> config = new UpdatedObservableAdapter<>(EDITING.component(componentName));
		if (Awaited.<EditableConfiguration>returnedValue(config, 1)) {
			openDialog(subTitle, config.getValue());
		};
	}

	private void openDialog(String subTitle, EditableConfiguration config) {
		EditConfigDialog editDialog = new EditConfigDialog(shell(), TITLE, subTitle, config, true, false);	
		if (editDialog.open() == Window.OK) {
			configService.write(editDialog.getComponent());
		}
	}
}
