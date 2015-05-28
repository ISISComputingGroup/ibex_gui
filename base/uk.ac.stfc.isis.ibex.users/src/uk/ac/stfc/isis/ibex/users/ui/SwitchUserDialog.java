package uk.ac.stfc.isis.ibex.users.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.users.Director;
import uk.ac.stfc.isis.ibex.users.Users;

public class SwitchUserDialog extends Dialog {
	
	private final Director director = Users.getInstance().director();
	private final UserSwitcherModel model = new UserSwitcherModel(director);
	
	public SwitchUserDialog(Shell parent) {
		super(parent);
	}
          
	@Override
    protected void configureShell(Shell newShell) { 
        super.configureShell(newShell); 
        newShell.setText("Switch user"); 
        newShell.setSize(250, 170); 
    } 

	@Override
	protected Control createDialogArea(Composite parent) { 
	    Composite composite = (Composite) super.createDialogArea(parent); 
	    UserSwitcher switcher = new UserSwitcher(composite, SWT.NONE);
	    switcher.setModel(model);
	    
	    updateButtons(false);
	    model.addPropertyChangeListener("isValid", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				updateButtons(model.getIsValid());
			}
		});
	    
	    return composite;
	}
	
    @Override
    protected void createButtonsForButtonBar(Composite parent) { 
        super.createButtonsForButtonBar(parent); 
    } 
	    
    @Override
    protected void okPressed() {
    	super.okPressed();	
    	director.switchUser(model.selectedUserName());
    }
    
    private void updateButtons(boolean isValid) { 
        Button okButton = getButton(IDialogConstants.OK_ID); 
        if (okButton != null) { 
            okButton.setEnabled(isValid); 
        }
    }
}
