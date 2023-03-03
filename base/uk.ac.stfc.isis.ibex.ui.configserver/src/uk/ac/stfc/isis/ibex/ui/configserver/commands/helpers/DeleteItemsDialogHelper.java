package uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers;

import java.util.Collection;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

/**
 * A dialog to confirm deleting items.
 */
public class DeleteItemsDialogHelper {
	/**
	 * Opens dialog to confirm deleting items.
	 * @param selectedItems Items that will be displayed in confirmation
	 * @param itemsType The type of items (eg. Configurations) to be deleted
	 * @return The open dialog.
	 */
    public boolean deleteItemsConfirmDialog(Collection<String> selectedItems, String itemsType) {
        return MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Confirm Delete " + itemsType,
                "The following " + itemsType.toLowerCase() + " " + selectedItems + " will be permanently deleted. Are you sure you want to delete them?");
    }
}
