package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.editing.DuplicateBlockNameException;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.validators.ErrorMessage;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;
import uk.ac.stfc.isis.ibex.validators.WarningMessage;

/**
 * A dialog for editing blocks.
 */
public class EditBlockDialog extends TitleAreaDialog {
	
	EditableConfiguration config;
	EditableBlock block;
    
	BlockDetailsPanel blockDetailsPanel;
	BlockDetailsViewModel blockDetailsViewModel;
	
    BlockRunControlPanel blockRunControlPanel;
    BlockRunControlViewModel blockRunControlViewModel;
    
    BlockLogSettingsPanel blockLogSettingsPanel;
    BlockLogSettingsViewModel blockLogSettingsViewModel;
	
	Button okButton;
	
	List<ErrorMessageProvider> viewModels = new ArrayList<>();
	
	private PropertyChangeListener errorListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            for (ErrorMessageProvider model : viewModels) {
                ErrorMessage error = model.getError();
                if (error.isError()) {
                    setErrorMessage(error.getMessage());
                    setOkEnabled(false);
                    return;
				}
			}
            setOkEnabled(true);
            setErrorMessage(null);
        }
    };

    private PropertyChangeListener warningListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            for (ErrorMessageProvider model : viewModels) {
                WarningMessage warning = model.getWarning();
                if (warning.isWarning()) {
                    setMessage(warning.getMessage(), IMessageProvider.WARNING);
                    return;
                }
            }
            setMessage(null);
        }
    };

    /**
     * Default constructor.
     * 
     * @param parentShell The shell of the parent element
     * @param block The block to edit
     * @param config The configuration the block belongs to
     */
    public EditBlockDialog(Shell parentShell, EditableBlock block, EditableConfiguration config) {
		super(parentShell);
        this.config = config;
        this.block = block;

        blockLogSettingsViewModel = new BlockLogSettingsViewModel(this.block);
		viewModels.add(blockLogSettingsViewModel);
		
        blockRunControlViewModel = new BlockRunControlViewModel(this.block);
		viewModels.add(blockRunControlViewModel);
		
        blockDetailsViewModel = new BlockDetailsViewModel(this.block, this.config);
		viewModels.add(blockDetailsViewModel);
		
		for (ErrorMessageProvider provider : viewModels) {
			provider.addPropertyChangeListener("error", errorListener);
			provider.addPropertyChangeListener("warning", warningListener);
        }

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
        try {
            if (!config.getAllBlocks().contains(this.block)) {
                config.addNewBlock(this.block);
            }
            super.okPressed();
        } catch (DuplicateBlockNameException e) {
                MessageDialog error = new MessageDialog(this.getShell(), "Error", null,
                        "Failed to add block " + this.block.getName() + ":\nBlock with this name already exists.",
                        MessageDialog.ERROR, new String[] {"OK"}, 0);
                error.open();
        }
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

        // Validate dialog, must be called here as buttons must exist
        blockDetailsViewModel.validate();
	}

    private void setOkEnabled(boolean enabled) {
    	okButton.setEnabled(enabled);
    }
}
