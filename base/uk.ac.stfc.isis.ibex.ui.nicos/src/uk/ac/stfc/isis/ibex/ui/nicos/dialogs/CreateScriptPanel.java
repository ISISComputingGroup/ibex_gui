package uk.ac.stfc.isis.ibex.ui.nicos.dialogs;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;

/**
 * The view for the dialog that creates a new script to send to the script server
 */
public class CreateScriptPanel extends Composite {

	public CreateScriptPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		StyledText styledText = new StyledText(this, SWT.BORDER);
	}
}
