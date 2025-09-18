package uk.ac.stfc.isis.ibex.ui.scriptgenerator.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


/**
 * A message dialog box for previewing a script before queuing to NICOS.
 */
public class QueueScriptPreviewDialog extends TitleAreaDialog {
	private Shell parentShell;
	private String generatedScript;
	
	/**
	 * Sets dialog window and data.
	 * @param parentShell Shell to open log dialog box
	 * @param generatedScript The generated script to preview
	 */
	public QueueScriptPreviewDialog(Shell parentShell, String generatedScript) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX); 
		this.generatedScript = generatedScript;
		this.parentShell = parentShell;
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Queue Script - Preview");
	}
	
	@Override
    @SuppressWarnings({ "checkstyle:magicnumber"})
	protected Control createDialogArea(Composite parent) {
		setTitle("Script Preview");
		setMessage("Preview the generated script before sending it to NICOS.");
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gl_container = new GridLayout(1, false);
		container.setLayout(gl_container);

		Text text = new Text(container, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL
							| SWT.V_SCROLL | SWT.MULTI);
		GridData gd_text = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_text.heightHint = 800;
		gd_text.widthHint = 800;
		
		text.setLayoutData(gd_text);
		text.setText(generatedScript);
		
		return container;
	}
	
	@Override
	public void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Queue Script", true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	
	/**
	 * Asks the user if they want to preview the script before sending to NICOS.
	 * @return Whether to preview the script or not.
	 */
	public Boolean askIfPreviewScript() {
		Boolean previewScript = MessageDialog.openQuestion(parentShell, "Preview Script", 
				"Preview the generated script before queuing?"); 
		return previewScript;
	}
}
