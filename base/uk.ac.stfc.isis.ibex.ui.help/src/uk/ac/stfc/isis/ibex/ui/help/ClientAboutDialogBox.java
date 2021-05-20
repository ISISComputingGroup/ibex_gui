package uk.ac.stfc.isis.ibex.ui.help;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.ui.about.AboutDialogBox;

public class ClientAboutDialogBox extends AboutDialogBox {

	public ClientAboutDialogBox(Shell parentShell) {
		super(parentShell, "IBEX");
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("About IBEX");
		
		Composite container = super.superCreateDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		
		new ClientVersionPanel(container, SWT.NONE);
		
		return container;
	}

}
