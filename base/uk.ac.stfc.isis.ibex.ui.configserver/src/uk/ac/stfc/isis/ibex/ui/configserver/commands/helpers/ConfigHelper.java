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
    protected String sub_title_current;
	
    protected ConfigurationViewModels configurationViewModels;
    protected Shell shell;
    
    /**
     * Create a dialog box for editing a config other than the current one.
     * 
     * @param configName The name of the config we wish to edit
     */
    public void createDialog(String configName) {
        String subTitle = "Viewing " + configName;

        configurationViewModels.setModelAsConfig(configName);
        UpdatedValue<EditableConfiguration> config = configurationViewModels.getConfigModel();

        if (Awaited.returnedValue(config, 1)) {
            openDialog(subTitle, config.getValue(), null, false);
        }
    }
    
    /**
     * Create a dialog box for editing the current config.
     * 
     * @param blockname The blockname we wish to edit, if null or empty we will
     *            edit the whole config.
     */
    public void createDialogCurrent(String blockname) {
        configurationViewModels.setModelAsCurrentConfig();
        UpdatedValue<EditableConfiguration> config = configurationViewModels.getConfigModel();

        if (Awaited.returnedValue(config, 1)) {
            openDialog(sub_title_current, config.getValue(), blockname, true);
        } else {
            MessageDialog.openError(shell, "Error", "There is no current configuration, so it can not be edited.");
        }
    }
    
    protected abstract void openDialog(String subTitle, EditableConfiguration config,
            String blockname, boolean isCurrent);
}
