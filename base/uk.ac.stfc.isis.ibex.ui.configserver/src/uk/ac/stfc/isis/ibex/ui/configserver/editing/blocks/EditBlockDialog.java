package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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
    BlockRunControlViewModel blockRunControlViewModel;
    
    BlockLogSettingsPanel blockLogSettingsPanel;
    BlockLogSettingsViewModel blockLogSettingsViewModel;
	
	Button okButton;
	
	private PropertyChangeListener errorListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ErrorMessage error = (ErrorMessage)evt.getNewValue();
				if (error.isError())
					setOkEnabled(false);
				else
					setOkEnabled(true);
				setErrorMessage(error.getMessage());
			}
		};

    protected EditBlockDialog(Shell parentShell, EditableBlock block, EditableConfiguration config) {
		super(parentShell);
		this.config = config;
		this.block = block;

		blockLogSettingsViewModel = new BlockLogSettingsViewModel(block);
		blockLogSettingsViewModel.addPropertyChangeListener("error", errorListener);
		
		blockRunControlViewModel = new BlockRunControlViewModel(block);
		blockRunControlViewModel.addPropertyChangeListener("error", errorListener);
	}
	
    @Override
    protected Control createDialogArea(Composite parent) {
        setTitle("Configure Block");

        blockDetailsPanel = new BlockDetailsPanel(parent, SWT.NONE, block, config);
        blockDetailsPanel.setLayout(new GridLayout(1, false));

        blockRunControlPanel = new BlockRunControlPanel(blockDetailsPanel, SWT.NONE,
        		blockRunControlViewModel);
        blockRunControlPanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        
        blockLogSettingsPanel = new BlockLogSettingsPanel(blockDetailsPanel, SWT.NONE,
        		blockLogSettingsViewModel);
        blockLogSettingsPanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

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

		blockRunControlViewModel.updateBlock();
        blockLogSettingsViewModel.updateBlock();
        
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
