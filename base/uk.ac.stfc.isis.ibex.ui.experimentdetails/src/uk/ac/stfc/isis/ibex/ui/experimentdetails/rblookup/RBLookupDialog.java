package uk.ac.stfc.isis.ibex.ui.experimentdetails.rblookup;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;

public class RBLookupDialog extends Dialog {

	private RBLookupPanel panel;
	private UserDetails selectedResult;
	
	public RBLookupDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {	
		//Prevent the return key from closing the dialog		
		getShell().addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent e) {
				if(e.detail == SWT.TRAVERSE_RETURN) {
					e.doit = false;
				}
			}
		});
		
		panel = new RBLookupPanel(parent, SWT.NONE, this);
		panel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		return panel;
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("RB Number Lookup");
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(550, 475);
	}
	
	@Override
	public void okPressed() {
		selectedResult = panel.getSelectedUser();
		super.okPressed();
	}
	
	@Override
	public void create() {
		super.create();
		setOKEnabled(false);
	}
	
	@Override
	public boolean close() {
		panel.close();
		return super.close();
	}
	
	public String getSelectedExpID() {
		return selectedResult.getAssociatedExperimentID();
	}
	
	public void setOKEnabled(boolean enabled) {
		Button okButton = getButton(IDialogConstants.OK_ID);
		okButton.setEnabled(enabled);
	}
}
