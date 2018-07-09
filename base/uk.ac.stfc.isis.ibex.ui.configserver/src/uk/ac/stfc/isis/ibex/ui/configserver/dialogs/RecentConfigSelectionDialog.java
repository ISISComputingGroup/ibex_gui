package uk.ac.stfc.isis.ibex.ui.configserver.dialogs;

import java.util.Collection;

import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.configuration.ConfigInfo;

/**
* Dialog for asking the user to select a single recent configuration or component.
*/
public class RecentConfigSelectionDialog extends MultipleRecentConfigsSelectionDialog {

    /**
     * @param parentShell The shell to create the dialog in.
     * @param title The title of the dialog box.
     * @param available A collection of the available configurations/components for the user to select from.
     * @param isComponent Whether the user is selecting from a list of components.
     * @param includeCurrent Whether the current configuration should be included in the list presented to the user.
     */
    public RecentConfigSelectionDialog(
            Shell parentShell, 
            String title,
            Collection<ConfigInfo> available, boolean isComponent, boolean includeCurrent) {
        super(parentShell, title, available, isComponent, includeCurrent);
        this.extraListOptions = 0;
    }
    
    /**
     * Get the name of the configuration/component that the user has chosen.
     * 
     * @return The chosen configuration/component.
     */
    public String selectedConfig() {
        return selected.toArray(new String[1])[0];
    }

    /**
     * @return A string corresponding to the type of item in the list.
     */
    @Override
    protected String getTypeString() {
        String plural = super.getTypeString();

        // Remove final s, if it exists
        String singular;
        if (plural != null && plural.length() > 0 && plural.charAt(plural.length() - 1) == 's') {
            singular = plural.substring(0, plural.length() - 1);
        } else {
            singular = plural;
        }

        return singular;
    }
}
