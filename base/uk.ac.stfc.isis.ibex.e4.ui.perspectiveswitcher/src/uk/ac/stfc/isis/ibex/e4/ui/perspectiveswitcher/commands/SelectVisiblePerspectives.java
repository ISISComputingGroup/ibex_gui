package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.commands;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.views.PerspectiveHidingDialog;

/**
 * Class containing the eclipse command to reset perspectives.
 */
public class SelectVisiblePerspectives {

    /**
     * Eclipse command to rest perspectives.
     */
    @Execute
    public void execute() {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        PerspectiveHidingDialog dialog = new PerspectiveHidingDialog(shell);
        dialog.open();
    }
}
