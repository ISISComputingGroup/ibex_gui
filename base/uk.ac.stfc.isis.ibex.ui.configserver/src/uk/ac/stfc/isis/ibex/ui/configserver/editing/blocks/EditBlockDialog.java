package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlServer;

public class EditBlockDialog extends TitleAreaDialog {
	
	EditableConfiguration config;
	EditableBlock block;
    RunControlServer runControl;
    
	BlockDetailsPanel blockDetailsPanel;
	BlockDetailsViewModel blockDetailsViewModel;
	
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
		
		blockDetailsViewModel = new BlockDetailsViewModel(block, config);
		blockDetailsViewModel.addPropertyChangeListener("error", errorListener);
	}
	
    @Override
    protected Control createDialogArea(Composite parent) {
        setTitle("Configure Block");

        blockDetailsPanel = new BlockDetailsPanel(parent, SWT.NONE, blockDetailsViewModel);
        blockDetailsPanel.setLayout(new GridLayout(1, false));

        blockRunControlPanel = new BlockRunControlPanel(blockDetailsPanel, SWT.NONE,
        		blockRunControlViewModel);
        blockRunControlPanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        
        blockLogSettingsPanel = new BlockLogSettingsPanel(blockDetailsPanel, SWT.NONE,
        		blockLogSettingsViewModel);
        blockLogSettingsPanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        
        return blockDetailsPanel;
    }
	
	@Override
	protected void okPressed() {
		blockDetailsViewModel.updateBlock();
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

    private void setOkEnabled(boolean enabled) {
    	okButton.setEnabled(enabled);
    }
}
