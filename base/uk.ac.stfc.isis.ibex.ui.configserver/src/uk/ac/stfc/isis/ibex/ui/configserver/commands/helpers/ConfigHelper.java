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
    protected String currentSubTitle;
	
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
        String subTitle = "Viewing " + configName;

        configurationViewModels.setModelAsConfig(configName);
        UpdatedValue<EditableConfiguration> config = configurationViewModels.getConfigModel();

        if (Awaited.returnedValue(config, 1)) {
            openDialog(subTitle, config.getValue(), false, editBlockFirst);
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
            openDialog(currentSubTitle, config.getValue(), true, editBlockFirst);
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
    
    protected abstract void openDialog(String subTitle, EditableConfiguration config, boolean isCurrent,
            boolean editBlockFirst);
}
