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

import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.QueuedScript;
import uk.ac.stfc.isis.ibex.ui.nicos.models.QueueScriptViewModel;

/**
 * The dialog for showing the content of a script in the script server.
 */
public abstract class ScriptDialog extends Dialog {
	/**
	 * The script.
	 */
	protected QueuedScript script;
	
	/**
	 * The viewmodel.
	 */
	protected QueueScriptViewModel model;
	
	/**
	 * The action button.
	 */
	protected Button actionButton;
	
	/**
	 * Whether this is a new script.
	 */
	protected boolean isNewScript;
	
	private static final Point INITIAL_SIZE = new Point(950, 800);
	
	/**
	 *  The constructor for this class.
	 *  
	 * @param parentShell The shell that this dialog is created from.
     * @param model The model for modifying the script queue
     * @param isNewScript Specifies whether it is an existing or new script
	 */
    public ScriptDialog(Shell parentShell, QueueScriptViewModel model, boolean isNewScript) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE);
		this.model = model;
		this.isNewScript = isNewScript;
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
	    
        ScriptContentPanel scriptPanel = new ScriptContentPanel(parent, SWT.NONE, script, this.isNewScript);
        scriptPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		return scriptPanel;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", true);
	}
	
}
