package uk.ac.stfc.isis.ibex.ui.scriptgenerator.dialogs;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.graphics.Point;

/**
 * Save parameters dialog to display when saving parameters in user given name.
 * @author mjq34833
 *
 */
public class SaveParametersDialog extends TitleAreaDialog {
	/**
	 * File name given by user .
	 */
	private String fileName;
	/**
	 * Text field.
	 */
	private Text txtName;
	/**
	 * OK button displayed to user.
	 */
	Button okButton;
	
	public SaveParametersDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	}
	
	@Override
    protected Control createDialogArea(Composite parent) {
        // create dialog
        setTitle("Save Parameter values to File");
        Composite container = (Composite) super.createDialogArea(parent);

        Composite composite = new Composite(container, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));
        GridData gdComposite = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gdComposite.widthHint = 95;
        composite.setLayoutData(gdComposite);
        
        Label lblConfigurationName = new Label(composite, SWT.NONE);
        lblConfigurationName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblConfigurationName.setText("Name:");

        txtName = new Text(composite, SWT.BORDER);
        GridData gdTxtName = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gdTxtName.widthHint = 300;
        txtName.setLayoutData(gdTxtName);
        txtName.setBounds(0, 0, 76, 21);
        txtName.addModifyListener(evt-> {
        	if (txtName.getText() == "") {
        		okButton.setEnabled(false);
        	} else {
        		okButton.setEnabled(true);
        	}
        });
        return container;
    }
	
    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText("Save Parameters");
    }
	

    /**
     * Create contents of the button bar.
     * 
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
    	okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    	okButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            }
        });
    	okButton.setEnabled(false);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }
    
    @Override
    protected void okPressed() {
    	fileName = txtName.getText();
    	super.okPressed();
    	close();
    }
    
    /**
     * Returns the user given name from this dialog box.
     * @return file name
     */
    public String getFileName() {
    	return fileName;
    }
    
    @Override
    protected Point getInitialSize() {
    	return new Point(400,200);
    }

}
