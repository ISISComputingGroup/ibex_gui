package uk.ac.stfc.isis.ibex.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

/**
 * Generic selection dialog class.
 */
@SuppressWarnings("checkstyle:magicnumber")
public abstract class SelectionDialog extends Dialog {

	private String title;
    /**
     * The selected items.
     */
    protected List items;
	
	/**
	 * @param parentShell The shell to open the dialog from.
	 * @param title The title of the dialog box.
	 **/
	protected SelectionDialog(Shell parentShell, String title) {
		super(parentShell);
		this.title = title;
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(title);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
	
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		createSelection(container);
		
		items.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				okPressed();
			}
		});
		
        items.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				getButton(IDialogConstants.OK_ID).setEnabled(items.getSelection().length != 0);
			}
		});
		
		return container;
	}
	
	@Override
	protected Control createButtonBar(Composite parent) {
		Control control = super.createButtonBar(parent);
		
		//Selection starts as null so disable ok button
		getButton(IDialogConstants.OK_ID).setEnabled(false);
		
		return control;
	}
	
	protected abstract void createSelection(Composite container);

}
