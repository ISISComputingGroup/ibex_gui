package uk.ac.stfc.isis.ibex.ui.help;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class AboutDialogBox extends TitleAreaDialog {

	public AboutDialogBox(Shell parentShell) {
		super(parentShell);		
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
        setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		setTitle("About IBEX");
		
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(300, 250);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("About IBEX");
		
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
			
		VersionPanel version = new VersionPanel(container, SWT.NONE);
		
		return container;
	}	

	@Override
	protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
		if (id == IDialogConstants.CANCEL_ID) {
			return null;
		}
		return super.createButton(parent, id, label, defaultButton);
	}
}
