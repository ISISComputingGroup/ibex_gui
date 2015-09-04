package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
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
		GridLayout gl_blockDetailsPanel = new GridLayout(1, false);
		gl_blockDetailsPanel.marginRight = 2;
		gl_blockDetailsPanel.marginLeft = 2;
		blockDetailsPanel.setLayout(gl_blockDetailsPanel);
		
		blockDetailsPanel.addNameModifyListener(new ModifyListener() {
		    @Override
		    public void modifyText(ModifyEvent nameText) {
		    	checkName(((Text) nameText.getSource()).getText());
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
	protected void cancelPressed() {
		config.removeBlock(block);		
		super.cancelPressed();
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Block Configuration");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okButton = createButton(parent, IDialogConstants.OK_ID, "Save", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false);
	}
	
	private void checkName(String name) {		
		setErrorMessage(null);
		setMessage(null);
		
		BlockNameValidator nameVal = new BlockNameValidator(config, block);
		Boolean nameIsValid = nameVal.validate(name);
				
		if (nameIsValid) {
			setSaveEnabled(true);
		} else {
			setErrorMessage(nameVal.getError());
			setSaveEnabled(false);
		}	
	}
	
    private void setSaveEnabled(boolean enabled) {
    	okButton.setEnabled(enabled);
    }
}
