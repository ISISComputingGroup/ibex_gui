package uk.ac.stfc.isis.ibex.ui.instrument;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;

public class InstrumentDialog extends Dialog {
	
	private final String TITLE = "Select an instrument";
	
	private InstrumentInfo selectedInstrument;
	private InstrumentSelectionPanel selector;
	
	public InstrumentInfo selectedInstrument() {
		return selectedInstrument;
	}
	
	protected InstrumentDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(TITLE );
	}

	@Override
	protected Point getInitialSize() {
		return new Point(300, 150);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		selector = new InstrumentSelectionPanel(parent, SWT.NONE, InstrumentUI.INSTRUMENT.instruments());
		selector.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		return container;
	}
	
	@Override
	protected void okPressed() {
		selectedInstrument = selector.getSelected();
		super.okPressed();
	}
}
