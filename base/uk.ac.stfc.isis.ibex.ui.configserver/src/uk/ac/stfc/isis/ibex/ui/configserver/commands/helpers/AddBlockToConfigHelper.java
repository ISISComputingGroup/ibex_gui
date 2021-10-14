package uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers;

import java.io.IOException;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.EditConfigDialog;

/**
 * A helper class to open block editing and configuration editing dialog boxes
 *
 */
public class AddBlockToConfigHelper extends EditConfigHelper {

	public AddBlockToConfigHelper(Shell shell, ConfigServer server) {
		super(shell, server);

	}

	/**
	 * Create the dialogs to edit the block and edit the configuration
	 * 
	 * @param config              Configuration selected by the user to add the
	 *                            block to
	 * @param isCurrent           If configuration is the current configuration
	 * @param editBlockFirst      Editing the block first before adding to
	 *                            configuration
	 * @param openEditBlockDialog Open edit block dialog
	 * @param pvName              PV that is added as block
	 * @throws IOException
	 */
	public void openDialogs(EditableConfiguration config, boolean isCurrent, boolean editBlockFirst,
			boolean openEditBlockDialog, String pvName) throws IOException {
		config.setIsComponent(false);
		final String configName = getConfigDisplayName(config, isCurrent);
		final String subTitle = "Editing the " + configName + " configuration";

		EditConfigDialog dialog = new EditConfigDialog(shell, title, subTitle, config, false, configurationViewModels,
				editBlockFirst, openEditBlockDialog, pvName); // Creating dialog to edit configuration
		dialog.addNew(config);// Open edit block dialog before editing the configuration
		if (dialog.open() == Window.OK) {// Edit configuration dialog

			server.saveAs().write(config.asConfiguration()); // Saving the block to the configuration

		}
	}

}
