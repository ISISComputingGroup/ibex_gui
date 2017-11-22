package uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.model.Awaited;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationViewModels;

/**
 * An interface for a class that is able to open a dialog giving information on the current configuration.
 */
public abstract class ConfigHelper {
    protected String title;
	
    protected ConfigurationViewModels configurationViewModels;
    protected Shell shell;
    
    /**
     * Create a dialog box for editing a config other than the current one.
     * 
     * @param configName
     *            The name of the config we wish to edit
     * @param editBlockFirst
     *            Whether to present the blocks tab first
     */
    public void createDialog(String configName, boolean editBlockFirst) {
        configurationViewModels.setModelAsConfig(configName);
        UpdatedValue<EditableConfiguration> config = configurationViewModels.getConfigModel();

        if (Awaited.returnedValue(config, 1)) {
            openDialog(config.getValue(), false, editBlockFirst);
        }
    }
    
    /**
     * Create a dialog box for editing the current config.
     * 
     * @param editBlockFirst
     *            Whether the first operation we want to do is edit a block
     */
    public void createDialogCurrent(boolean editBlockFirst) {
        configurationViewModels.setModelAsCurrentConfig();
        UpdatedValue<EditableConfiguration> config = configurationViewModels.getConfigModel();

        if (Awaited.returnedValue(config, 1)) {
            openDialog(config.getValue(), true, editBlockFirst);
        } else {
            MessageDialog.openError(shell, "Error", "There is no current configuration, so it can not be edited.");
        }
    }

    /**
     * Create a dialog box for editing the current config.
     */
    public void createDialogCurrent() {
        createDialogCurrent(false);
    }
    
    /**
     * Opens a view/edit configuration dialog for the specified configuration.
     * 
     * @param config
     *            The configuration to open
     * @param isCurrent
     *            Is this the current configuration
     * @param editBlockFirst
     *            Should the dialog open the blocks tab first
     */
    protected abstract void openDialog(EditableConfiguration config, boolean isCurrent,
            boolean editBlockFirst);
    
    /**
     * Get the display name for this configuration
     * 
     * This will return the configuration name or current if it is the current configuration. 
     * @param config
     * 			The configuration to get the name from
     * @param isCurrent
     * 			Is this the current configuration
     * @return the display name for this configuration
     */
    public String getConfigDisplayName(EditableConfiguration config, boolean isCurrent) {
    	return (isCurrent) ? "current" : config.getName();
    }
}
