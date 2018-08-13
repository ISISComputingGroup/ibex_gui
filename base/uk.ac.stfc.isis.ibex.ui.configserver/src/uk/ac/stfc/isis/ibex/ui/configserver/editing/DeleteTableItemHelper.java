package uk.ac.stfc.isis.ibex.ui.configserver.editing;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.epics.observing.INamed;

public class DeleteTableItemHelper<T extends INamed> {
	Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	
    public int createDeleteDialog(String itemType, List<T> toRemove) {
        String dialogTitle = "Delete " + itemType;
        String dialogText = "Do you really want to delete the " + itemType;
        
        if (toRemove.size() == 1) {
            dialogText += " " + toRemove.get(0).getName() + "?";
        } else {
            dialogTitle = "Delete " + itemType + "s";
            dialogText += "s " + itemNamesToString(toRemove) + "?";
        }
                
        MessageBox dialog = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
        dialog.setText(dialogTitle);
        dialog.setMessage(dialogText);
        return dialog.open();
    }
	
	private String itemNamesToString(List<T> items) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            INamed item = items.get(i);
            sb.append(item.getName());
            if (i == items.size() - 2) {
                sb.append(" and ");
            } else if (i != items.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
