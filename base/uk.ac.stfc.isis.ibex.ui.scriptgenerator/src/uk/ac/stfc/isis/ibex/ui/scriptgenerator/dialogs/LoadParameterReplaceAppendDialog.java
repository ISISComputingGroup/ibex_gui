package uk.ac.stfc.isis.ibex.ui.scriptgenerator.dialogs;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


/**
 * A dialog box to ask the user whether they wish to replace the current actions with the ones loaded from a file 
 * or just append the loaded actions
 * 
 * @author Adam Laverack
 *
 */
public class LoadParameterReplaceAppendDialog extends Dialog {
	
	// replace the parameters or not?
	private boolean replace;
	
	/**
	 * Constructor
	 * 
	 * @param parentShell A shell to attach the dialog to
	 */
	public LoadParameterReplaceAppendDialog(Shell parentShell) {
		super(parentShell);
	}
	
    /**
     * Set the size of the dialog box
     */
    @Override
    protected Point getInitialSize() {
        return new Point(500, 150);
    }
	
	/**
	 * Set the title of the dialog box
	 * 
	 * @param newShell The shell to attach the dialog to
	 */
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Load Parameters");
    }
    
	/**
	 * Create the area of the dialog with text in.
	 * 
	 * @param parent The composite parent to display the message area in.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container =  (Composite) super.createDialogArea(parent);
		
		Label mainText = new Label(parent, SWT.CENTER | SWT.WRAP);
		mainText.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		mainText.setText("Would you like to the loaded parameters to be appended to the current actions or to replace the current actions?");
		
		return container;
	}

	/**
	 * Create the buttons
	 * 
	 * @param parent The parent composite
	 */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
		parent.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		
		Button appendButton = createButton(parent, IDialogConstants.OK_ID, "Append", true);
		appendButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		appendButton.addSelectionListener(widgetSelectedAdapter(event -> {
			this.replace = false;
		}));
		
		Button replaceButton = createButton(parent, IDialogConstants.OK_ID, "Replace", false);
		replaceButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		replaceButton.addSelectionListener(widgetSelectedAdapter(event -> {
			this.replace = true;
		}));
    }
    
    public boolean getAnswer() {
    	return this.replace;
    }
    
    /**
     * Open a dialog
     * 
     * @param parent Shell to attach the dialog to
     * @param filepath Path of file to load parameters from
     * @param scriptGeneratorModel The ScriptGeneratorSingleton
     */
    public static boolean openDialog(Shell parent) {
		LoadParameterReplaceAppendDialog newdialog = new LoadParameterReplaceAppendDialog(parent);
		newdialog.open();
		return newdialog.getAnswer();
    }
}
