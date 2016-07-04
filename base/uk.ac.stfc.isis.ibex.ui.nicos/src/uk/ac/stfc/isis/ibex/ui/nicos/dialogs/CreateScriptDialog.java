package uk.ac.stfc.isis.ibex.ui.nicos.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * The dialog for creating a new script to send to the script server
 */
public class CreateScriptDialog extends Dialog {

	private static final Point INITIAL_SIZE = new Point(950, 800);
	
	private CreateScriptPanel creator;
    private Button sendBtn;

    public CreateScriptDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
        creator = new CreateScriptPanel(parent, SWT.NONE);
        creator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		return creator;
	}

	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
        sendBtn = createButton(parent, IDialogConstants.OK_ID, "Send", false);
        sendBtn.addSelectionListener(new SelectionAdapter() {
        	//TODO
        });
		
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", true);
	}	
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Create Script");
	}
	
	@Override
	protected Point getInitialSize() {
		return INITIAL_SIZE;
	}
}
