package uk.ac.stfc.isis.ibex.ui.nicos.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.QueuedScript;
import uk.ac.stfc.isis.ibex.ui.nicos.models.QueueScriptViewModel;

/**
 * The dialog for showing the details of a script that is on the NICOS queue.
 */
public class ScriptDialog extends Dialog {
	protected QueuedScript script;
	protected QueueScriptViewModel model;
	protected Button actionButton;
	
	private static final Point INITIAL_SIZE = new Point(950, 800);
	
	/**
	 *  The constructor for this class.
	 *  
	 * @param parentShell The shell that this dialog is created from.
	 */
    public ScriptDialog(Shell parentShell, QueueScriptViewModel model) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE);
		this.model = model;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Point getInitialSize() {
		return INITIAL_SIZE;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
        new QueuedScriptToolbar(parent, script, true);
	    
        EditScriptPanel scriptPanel = new EditScriptPanel(parent, SWT.NONE, script);
        scriptPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		return scriptPanel;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", true);
	}
	
}
