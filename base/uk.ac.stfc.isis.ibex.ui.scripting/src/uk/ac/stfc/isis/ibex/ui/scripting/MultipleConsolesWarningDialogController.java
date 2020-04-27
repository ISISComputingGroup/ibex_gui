package uk.ac.stfc.isis.ibex.ui.scripting;

import org.eclipse.swt.widgets.Button;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

public class MultipleConsolesWarningDialogController extends WorkbenchWindowControlContribution {
	
	private static final String MESSAGE = "Enable multiple consoles warning dialog";
	private Button button;
	
	@Override
	protected Control createControl(Composite parent) {
		button = new Button(parent, SWT.CHECK);
		button.setToolTipText(MESSAGE);
		button.setSelection(true);
		button.addSelectionListener(new SelectionAdapter() {

	        @Override
	        public void widgetSelected(SelectionEvent event) {
	            Button btn = (Button) event.getSource();
	            Consoles.showDialog = btn.getSelection();
	        }
	    });
		
		
		return button;
	
	}
}