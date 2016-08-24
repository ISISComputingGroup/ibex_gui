package uk.ac.stfc.isis.ibex.ui.scriptgenerator;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ButtonsPanel extends Composite {
	public ButtonsPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, true));
		
		Button btnPreview = new Button(this, SWT.NONE);
		btnPreview.setText("Preview Script");
		btnPreview.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnWrite = new Button(this, SWT.NONE);
		btnWrite.setText("Write Script");
		btnWrite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
	}
}
