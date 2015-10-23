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
import uk.ac.stfc.isis.ibex.validators.PvValidator;

public class EditBlockDialog extends TitleAreaDialog {
	
	EditableConfiguration config;
	EditableBlock block;
    RunControlServer runControl;
	BlockDetailsPanel blockDetailsPanel;
    BlockRunControlPanel blockRunControlPanel;
    BlockLogSettingsPanel blockLogSettingsPanel;
	
	Button okButton;

    protected EditBlockDialog(Shell parentShell, EditableBlock block, EditableConfiguration config) {
		super(parentShell);
		this.config = config;
		this.block = block;

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

        blockRunControlPanel = new BlockRunControlPanel(blockDetailsPanel, SWT.NONE, block);
        blockRunControlPanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        blockLogSettingsPanel = new BlockLogSettingsPanel(blockDetailsPanel, SWT.NONE,
                new BlockLogSettingsViewModel(block));
        blockRunControlPanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        blockDetailsPanel.addNameModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent nameText) {
                checkName(((Text) nameText.getSource()).getText());
            }
        });

        blockDetailsPanel.addAddressModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent addressText) {
                checkAddress(((Text) addressText.getSource()).getText());
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
		
        block.setRCLowLimit(blockRunControlPanel.getLowLimit());
        block.setRCHighLimit(blockRunControlPanel.getHighLimit());
        block.setRCEnabled(blockRunControlPanel.getEnabledSetting());

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
	
    private void checkAddress(String address) {
        setErrorMessage(null);
        setMessage(null);

        PvValidator addressValid = new PvValidator();
        Boolean addressIsValid = addressValid.validatePvAddress(address);

        if (addressIsValid) {
            setOkEnabled(true);
        } else {
            setErrorMessage(addressValid.getErrorMessage());
            setOkEnabled(false);
        }
    }

    private void setOkEnabled(boolean enabled) {
    	okButton.setEnabled(enabled);
    }
}
