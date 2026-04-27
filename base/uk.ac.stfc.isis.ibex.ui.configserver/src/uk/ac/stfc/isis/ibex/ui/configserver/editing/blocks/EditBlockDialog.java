/**
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2025
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */
package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.eclipse.jface.dialogs.IDialogConstants;
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
import uk.ac.stfc.isis.ibex.ui.widgets.buttons.IBEXHelpButton;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;

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
    
    BlockGroupPanel blockGroupPanel;
    BlockGroupViewModel blockGroupViewModel;
    
    BlockSetPanel blockSetPanel;
    BlockSetViewModel blockSetViewModel;
    
    BlockAlarmConfigPanel blockAlarmConfigPanel;
    BlockAlarmConfigViewModel blockAlarmConfigViewModel;
	
	Button okButton;
	
	private static final String HELP_LINK = "https://shadow.nd.rl.ac.uk/ibex_user_manual/how_to/Create-and-Manage-Configurations.html#blocks-tab";
	private static final String DESCRIPTION = "Configure Block Dialog";
	
	private final List<ErrorMessageProvider> viewModels;
	
	private PropertyChangeListener errorListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Optional<String> error = viewModels.stream()
            		.filter(model -> model.getError().isError())
            		.map(model -> model.getError().getMessage())
            		.findFirst();
            
            setOkEnabled(!error.isPresent());
            setErrorMessage(error.orElse(null));
        }
    };

    private PropertyChangeListener warningListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Optional<String> warning = viewModels.stream()
            		.filter(model -> model.getWarning().isWarning())
            		.map(model -> model.getWarning().getMessage())
            		.findFirst();
            
            setMessage(warning.orElse(null));
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
        blockRunControlViewModel = new BlockRunControlViewModel(this.block);
        blockDetailsViewModel = new BlockDetailsViewModel(this.block, this.config);
        blockGroupViewModel = new BlockGroupViewModel(this.block, this.config);
        blockSetViewModel = new BlockSetViewModel(this.block);
        blockAlarmConfigViewModel = new BlockAlarmConfigViewModel(this.block);
        
		
		viewModels = Arrays.asList(blockLogSettingsViewModel, blockRunControlViewModel, blockDetailsViewModel,
				blockGroupViewModel, blockAlarmConfigViewModel);
		
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
        
        blockGroupPanel = new BlockGroupPanel(blockDetailsPanel, SWT.NONE,
        		blockGroupViewModel);
        blockGroupPanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        
        blockSetPanel = new BlockSetPanel(blockDetailsPanel, SWT.NONE,
        		blockSetViewModel);
        blockSetPanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        
		blockAlarmConfigPanel = new BlockAlarmConfigPanel(blockDetailsPanel, SWT.NONE, blockAlarmConfigViewModel);
		blockAlarmConfigPanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        new IBEXHelpButton(parent, HELP_LINK, DESCRIPTION);
        
        return blockDetailsPanel;
    }
	
	@Override
	protected void okPressed() {
		blockDetailsViewModel.updateBlock();
		blockRunControlViewModel.updateBlock();
        blockLogSettingsViewModel.updateBlock();
        blockGroupViewModel.updateBlock();
        blockSetViewModel.updateBlock();
        blockAlarmConfigViewModel.updateBlock();
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
