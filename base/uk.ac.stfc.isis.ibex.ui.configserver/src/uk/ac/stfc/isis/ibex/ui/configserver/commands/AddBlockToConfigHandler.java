package uk.ac.stfc.isis.ibex.ui.configserver.commands;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.csstudio.csdata.ProcessVariable;
import org.csstudio.csdata.TimestampedPV;
import org.csstudio.ui.util.AdapterUtil;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationServerUI;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationViewModels;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers.AddBlockToConfigHelper;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.ConfigSelectionDialog;

/**
 * The handler class for adding block to a chosen configuration by the user.
 *
 */
public class AddBlockToConfigHandler extends AbstractHandler {

	static final ConfigServer SERVER = Configurations.getInstance().server();
	static final Shell SHELL = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	String configName;

	/**
	 * Open the configuration selection dialog and adding new block to the selected
	 * configuration.
	 * 
	 * @param event The event that caused the open.
	 * @throws ExecutionException
	 * @return null
	 */

	public Object execute(ExecutionEvent event) throws ExecutionException {

		try {

			String pvAddress = getPVFromEvent(event);
			createConfigDialog(pvAddress);

		} catch (Exception ex) {
			MessageDialog.openError(SHELL, "Error", ex.getMessage());
		}

		return null;
	}
	
	/**
	 * Prevent adding block without permission.
	 */
	@Override
	public boolean isEnabled() {
		return super.isEnabled() && SERVER.writableConfigsExist();
	}

	/**
	 * Create dialog to select configuration, open edit configuration and add new
	 * block dialog
	 * 
	 * @param pvAddress pv that is selected by the user to add as a block to the
	 *                  configuration
	 * 
	 * @throws TimeoutException
	 * @throws IOException
	 */
	private void createConfigDialog(String pvAddress) throws TimeoutException, IOException {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(); // Gets the shell
		AddBlockToConfigHelper helper = new AddBlockToConfigHelper(shell, SERVER);
		String titleText = "Select configuration";

		// Create configuration selection dialog
		ConfigSelectionDialog configSelectionDialog = new ConfigSelectionDialog(shell, titleText,
				SERVER.configsInfo().getValue(), SERVER.configNamesWithFlags(), false, true);

		if (configSelectionDialog.open() == Window.OK) {
			configName = configSelectionDialog.selectedConfig(); // Name of the selected configuration
			ConfigurationViewModels configurationViewModels = ConfigurationServerUI.getDefault()
					.configurationViewModels();
			// Get configuration as EditableConfiguration object
			EditableConfiguration config = configurationViewModels.getConfig(configName);
			// Open block edit dialog and edit configuration dialog from helper
			helper.openDialogs(config, false, true, true, pvAddress);

		}

	}

	/**
	 * Gets the first PV address from the selection.
	 * 
	 * @param event The selection that caused the open
	 * @throws Exception if no PV could be found in the selection
	 * @return The PV address
	 */
	private String getPVFromEvent(ExecutionEvent event) throws Exception {
		String pvAddress = "";

		ISelection selection = HandlerUtil.getActiveMenuSelection(event);
		if (selection == null) {
			// This works for double-clicks.
			selection = HandlerUtil.getCurrentSelection(event);
		}

		// Add received PVs with default archive data sources
		final List<TimestampedPV> timestampedPVs = Arrays.asList(AdapterUtil.convert(selection, TimestampedPV.class));
		if (!timestampedPVs.isEmpty()) {
			pvAddress = timestampedPVs.get(0).getName();
		} else {
			final List<ProcessVariable> pvs = Arrays.asList(AdapterUtil.convert(selection, ProcessVariable.class));
			if (!pvs.isEmpty()) {
				pvAddress = pvs.get(0).getName();
			} else {
				throw new Exception("No PV found for selection");
			}
		}

		return pvAddress;
	}
}
