package uk.ac.stfc.isis.ibex.ui.nicos.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.ui.nicos.models.QueueScriptViewModel;

/**
 * The dialog for showing the details of a script that is on the NICOS queue.
 */
public class EditScriptDialog extends ScriptDialog {
	
	/**
	 *  The constructor for this class.
	 *  
	 * @param parentShell The shell that this dialog is created from.
	 * @param script The script to display the contents of.
	 */
    public EditScriptDialog(Shell parentShell, QueueScriptViewModel model) {
		super(parentShell, model);
        this.script = model.getSelectedScript();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
        actionButton = createButton(parent, IDialogConstants.OK_ID, "Update", false);
        actionButton.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.alarm", "icons/refresh.png"));
        actionButton.addListener(SWT.Selection, e -> model.queueScript());
		super.createButtonsForButtonBar(parent);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
        shell.setText(script.getName());
	}
}
