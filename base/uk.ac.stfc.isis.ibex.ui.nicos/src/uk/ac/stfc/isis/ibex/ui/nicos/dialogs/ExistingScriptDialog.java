package uk.ac.stfc.isis.ibex.ui.nicos.dialogs;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.QueuedScript;
import uk.ac.stfc.isis.ibex.ui.widgets.NumberedStyledText;

/**
 * The dialog for showing the details of a script that is on the NICOS queue.
 */
public class ExistingScriptDialog extends Dialog {
	private QueuedScript script;
	
	private static final Point INITIAL_SIZE = new Point(950, 800);
	
	/**
	 *  The constructor for this class.
	 *  
	 * @param parentShell The shell that this dialog is created from.
	 * @param script The script to display the contents of.
	 */
    public ExistingScriptDialog(Shell parentShell, QueuedScript script) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE);
        this.script = script;
	}
	
    /**
     * {@inheritDoc}
     */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
        StyledText codeText = new NumberedStyledText(container, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL);
        
        DataBindingContext bindingContext = new DataBindingContext();
        
        bindingContext.bindValue(WidgetProperties.text().observe(codeText),
                BeanProperties.value("code").observe(script));
        
		return container;
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {

		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
        shell.setText(script.getName());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Point getInitialSize() {
		return INITIAL_SIZE;
	}
}
