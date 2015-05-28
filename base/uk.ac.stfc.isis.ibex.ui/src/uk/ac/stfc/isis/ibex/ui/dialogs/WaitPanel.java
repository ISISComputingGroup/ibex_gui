package uk.ac.stfc.isis.ibex.ui.dialogs;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class WaitPanel extends Composite {

	public WaitPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		Label lblWaitingForServer = new Label(this, SWT.CENTER);
		lblWaitingForServer.setAlignment(SWT.CENTER);
		lblWaitingForServer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		lblWaitingForServer.setText("Waiting for the instrument to update");
	}
}
