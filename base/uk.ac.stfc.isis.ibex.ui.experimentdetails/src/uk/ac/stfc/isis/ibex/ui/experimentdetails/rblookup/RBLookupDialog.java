package uk.ac.stfc.isis.ibex.ui.experimentdetails.rblookup;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * A dialog for searching and looking up an RB number.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class RBLookupDialog extends Dialog {

	private RBLookupPanel panel;
	private DataBindingContext bindingContext;
	private RBLookupViewModel viewModel;
	
	/**
	 * Creates a new RB lookup dialog.
	 *
	 * @param parentShell the parent shell
	 * @param viewModel the view model
	 */
	public RBLookupDialog(Shell parentShell, RBLookupViewModel viewModel) {
		super(parentShell);
		this.viewModel = viewModel;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {	
		//Prevent the return key from closing the dialog		
		getShell().addTraverseListener(e -> {
			if (e.detail == SWT.TRAVERSE_RETURN) {
				e.doit = false;
			}
		});
		
		panel = new RBLookupPanel(parent, SWT.NONE);
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
		super.okPressed();
	}
	
	@Override
	public void create() {
		super.create();
		setModel();
	}
	
	/**
	 * Sets the model and binds all of the values.
	 */
	public void setModel() {
		bindingContext = new DataBindingContext();

		Button okButton = getButton(IDialogConstants.OK_ID);
		bindingContext.bindValue(WidgetProperties.enabled().observe(okButton), BeanProperties.value("okEnabled").observe(viewModel));
		
		panel.setModel(viewModel);
	}
}
