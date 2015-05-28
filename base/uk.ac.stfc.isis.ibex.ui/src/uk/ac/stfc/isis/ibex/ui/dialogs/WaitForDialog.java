package uk.ac.stfc.isis.ibex.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class WaitForDialog extends Dialog {
	
	private static final Point INITIAL_SIZE = new Point(250, 100);
	
	public WaitForDialog(Shell parentShell) {
		super(parentShell);
	    super.setShellStyle(SWT.ALPHA);	  
	}

	@Override
	protected Control createDialogArea(Composite parent) {	
		Composite panel = new WaitPanel(parent, SWT.NONE);
		panel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		return panel;
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// No buttons
	}
	
	@Override
	public int open() {
		setCursor(SWT.CURSOR_WAIT);
		getParentShell().setEnabled(false);        
		return super.open();
	}
	
	@Override
	public boolean close() {
		getParentShell().setEnabled(true);	
		setCursor(SWT.CURSOR_ARROW);

		return super.close();
	}

	public void setCursor(int cursorType) {
		Cursor cursor = Display.getDefault().getSystemCursor(cursorType);
		Shell activeShell = Display.getDefault().getActiveShell();
		if (activeShell != null) {
			activeShell.setCursor(cursor);
		}
	}
	
	@Override
	protected Point getInitialSize() {
		return INITIAL_SIZE;
	}
}
