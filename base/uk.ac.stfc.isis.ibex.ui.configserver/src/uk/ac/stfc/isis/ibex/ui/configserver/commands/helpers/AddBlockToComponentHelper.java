package uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers;

import java.io.IOException;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.EditConfigDialog;

/**
 * A helper class to open block editing and component editing dialog boxes
 *
 */
public class AddBlockToComponentHelper extends EditComponentHelper {
	public AddBlockToComponentHelper(Shell shell, ConfigServer server) {
		super(shell, server);

	}

	/**
	 * Create the dialogs to edit the block and edit the component
	 * 
	 * @param component           Component selected by the user to add the block to
	 * @param isCurrent           If component is current component
	 * @param editBlockFirst      Editing the block first before adding to component
	 * @param openEditBlockDialog Open edit block dialog
	 * @param pvName              PV that is added as block
	 * @throws IOException
	 */
	public void openDialogs(EditableConfiguration component, boolean isCurrent, boolean editBlockFirst,
			boolean openEditBlockDialog, String pvName) throws IOException {
		component.setIsComponent(true);
		final String componentName = getConfigDisplayName(component, isCurrent);
		final String subTitle = "Editing the " + componentName + " component";
		final String title = "Edit Component";
		// Creating dialog to edit component
		EditConfigDialog dialog = new EditConfigDialog(shell, title, subTitle, component, false,
				configurationViewModels, editBlockFirst, openEditBlockDialog, pvName);
		dialog.addNew(component);// Open edit block dialog before editing the component
		if (dialog.open() == Window.OK) {// Edit component dialog

			server.saveAsComponent().write(dialog.getComponent()); // Saving the block to the component

		}
	}

}