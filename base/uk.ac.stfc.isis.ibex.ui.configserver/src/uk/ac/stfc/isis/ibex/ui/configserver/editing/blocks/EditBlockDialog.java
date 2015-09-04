package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;

public class EditBlockDialog extends TitleAreaDialog {
	
	EditableConfiguration config;
	EditableBlock block;
	BlockDetailsPanel blockDetailsPanel;
	
	Button okButton;

	protected EditBlockDialog(Shell parentShell, EditableBlock block, EditableConfiguration config) {
		super(parentShell);
		this.config = config;
		this.block = block;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("Configure Block");
		blockDetailsPanel = new BlockDetailsPanel(parent, SWT.NONE, block, config);
		
		blockDetailsPanel.addNameModifyListener(new ModifyListener() {
		    @Override
		    public void modifyText(ModifyEvent arg0) {
		    	checkName(arg0);
		    }
		});
		
		return blockDetailsPanel;
	}
	
	@Override
	protected void okPressed() {
		block.setName(blockDetailsPanel.getBlockName());
		block.setPV(blockDetailsPanel.getPV());
		block.setIsLocal(blockDetailsPanel.getIsLocal());
		block.setIsVisible(blockDetailsPanel.getIsVisible());
		
		super.okPressed();
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Block Configuration");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okButton = createButton(parent, IDialogConstants.OK_ID, "Save", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", true);
	}
	
	private void checkName(ModifyEvent arg0) {		
		setErrorMessage(null);
		setMessage(null);
		
		String name = ((Text) arg0.getSource()).getText();
				
		if (name.toString().length() == 0) {
			setErrorMessage("Please enter a name for the block");
			setSaveEnabled(false);
		} else if (!validateName(name)) {
			setErrorMessage("Name must start with a letter, and contain letters, numbers and underscores only");
			setSaveEnabled(false);
		} else {
			setSaveEnabled(true);
		}		
	}
	
    private Boolean validateName(String name) {
		return name.matches("^[a-zA-Z][a-zA-Z0-9_]*$");
    }
    
    private void setSaveEnabled(boolean enabled) {
    	okButton.setEnabled(enabled);
    }    
}
