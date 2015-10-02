package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.configserver.editing.BlockNameValidator;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlServer;

public class EditBlockDialog extends TitleAreaDialog {
	
	EditableConfiguration config;
	EditableBlock block;
    RunControlServer runControl;
	BlockDetailsPanel blockDetailsPanel;
    BlockRunControlPanel blockRunControlPanel;
	
	Button okButton;

    protected EditBlockDialog(Shell parentShell, EditableBlock block, EditableConfiguration config,
            RunControlServer runControl) {
		super(parentShell);
		this.config = config;
		this.block = block;
        this.runControl = runControl;

        setTitle("Configure Block");
        blockDetailsPanel = new BlockDetailsPanel(parentShell, SWT.NONE, block, config);
        blockDetailsPanel.setLayout(new GridLayout(1, false));

        blockDetailsPanel.addNameModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent nameText) {
                checkName(((Text) nameText.getSource()).getText());
            }
        });
	}
	
    @Override
    protected Control createDialogArea(Composite parent) {
        setTitle("Configure Block");

        blockDetailsPanel = new BlockDetailsPanel(parent, SWT.NONE, block, config);
        blockDetailsPanel.setLayout(new GridLayout(1, false));

        blockRunControlPanel = new BlockRunControlPanel(blockDetailsPanel, SWT.NONE, block.getName(), runControl);
        blockRunControlPanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

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
		
        runControl.blockRunControlLowLimitSetter(block.getName()).write(blockRunControlPanel.getLowLimit());
        runControl.blockRunControlHighLimitSetter(block.getName()).write(blockRunControlPanel.getHighLimit());
        runControl.blockRunControlEnabledSetter(block.getName()).write(blockRunControlPanel.getEnabledSetting());

        blockRunControlPanel.removeObservers();

		super.okPressed();
	}
	
	@Override
	protected void cancelPressed() {
		config.removeBlock(block);		
        blockRunControlPanel.removeObservers();
		super.cancelPressed();
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Block Configuration");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okButton = createButton(parent, IDialogConstants.OK_ID, "OK", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false);
	}
	
	private void checkName(String name) {		
		setErrorMessage(null);
		setMessage(null);
		
		BlockNameValidator nameVal = new BlockNameValidator(config, block);
		Boolean nameIsValid = nameVal.isValidName(name);
				
		if (nameIsValid) {
			setOkEnabled(true);
		} else {
			setErrorMessage(nameVal.getErrorMessage());
			setOkEnabled(false);
		}	
	}
	
    private void setOkEnabled(boolean enabled) {
    	okButton.setEnabled(enabled);
    }
}
