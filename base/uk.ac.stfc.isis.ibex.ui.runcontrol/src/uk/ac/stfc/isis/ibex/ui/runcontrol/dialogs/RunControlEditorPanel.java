
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
*
* You should have received a copy of the Eclipse Public License v1.0
* along with this program; if not, you can obtain a copy from
* https://www.eclipse.org/org/documents/epl-v10.php or 
* http://opensource.org/licenses/eclipse-1.0.php
*/

package uk.ac.stfc.isis.ibex.ui.runcontrol.dialogs;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.epics.writing.OnCanWriteChangeListener;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.EditBlockHandler;
import uk.ac.stfc.isis.ibex.ui.runcontrol.RunControlViewModel;

/**
 * A panel to edit the run control settings for the selected block.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class RunControlEditorPanel extends Composite {
    private static final String RESET_ALL_DIALOG_TITLE = "Confirm Run-Control Restore";
    private static final String RESET_ALL_DIALOG_MESSAGE = "Are you sure you want to restore all run-control settings to their configuration values?";
	private static final String CONFIRM_CHANGES_HINT = "WARNING! The settings applied here are not permanent and will be overriden if current configuration changes (see: User Manual - Run Controls)";
	private final Label name;
	private final Text txtLowLimit;
	private final Text txtHighLimit;
	private final Button chkEnabled;
	private final Button chkInvalid;
	private final Button btnSend;
    private final Button btnRestoreSingle;
    private Button btnRestoreAll;
	private final Button btnDisplayBlockInfo;
    private final Label spacerLabel;
    private final Label spacerLabel2;
    private final Label spacerLabel3;
    private Group grpGlobalSettings;
    private Group grpAdditionalSection;
    private boolean canSend;
    private DisplayBlock currentBlock;

    private final RunControlViewModel viewModel;
	
	private OnCanWriteChangeListener canWriteListener = canWrite -> {
        canSend = canWrite;
        btnRestoreAll.setEnabled(canWrite);
	};

    /**
     * Creates the panel for editing the run control on one block.
     * 
     * @param parent
     *            The parent composite.
     * @param style
     *            The SWT style of the panel.
     * @param configServer
     *            The config server object used to write to the instrument.
     * @param viewModel
     *            The view model for this panel.
     * @param dialog
     * 			  The initial dialog that opened this panel.
     *            
     */
    public RunControlEditorPanel(EditRunControlDialog dialog, Composite parent, int style, ConfigServer configServer,
            final RunControlViewModel viewModel) {
		super(parent, style);
        this.viewModel = viewModel;

        setLayout(new GridLayout(3, false));

		Group grpSelectedSetting = new Group(this, SWT.NONE);
        grpSelectedSetting.setText("Block Settings");
        grpSelectedSetting.setLayout(new GridLayout(9, false));
		
		Label lblName = new Label(grpSelectedSetting, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");
		
		name = new Label(grpSelectedSetting, SWT.NONE);
        GridData gdLblName = new GridData(SWT.FILL, SWT.CENTER, false, false, 8, 1);
		gdLblName.widthHint = 150;
		name.setLayoutData(gdLblName);
		
		Label lblLowLimit = new Label(grpSelectedSetting, SWT.NONE);
		lblLowLimit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLowLimit.setText("Low Limit:");
		
		txtLowLimit = new Text(grpSelectedSetting, SWT.BORDER);
		GridData gdTxtLow = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
        gdTxtLow.widthHint = 50;
		txtLowLimit.setLayoutData(gdTxtLow);
		
        spacerLabel = new Label(grpSelectedSetting, SWT.NONE);
        spacerLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        spacerLabel.setText(" ");

		Label lblHighLimit = new Label(grpSelectedSetting, SWT.NONE);
		lblHighLimit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHighLimit.setText("High Limit:");
		
		txtHighLimit = new Text(grpSelectedSetting, SWT.BORDER);
		GridData gdTxtHigh = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
        gdTxtHigh.widthHint = 50;
		txtHighLimit.setLayoutData(gdTxtHigh);
		
        spacerLabel2 = new Label(grpSelectedSetting, SWT.NONE);
        spacerLabel2.setText(" ");

		chkEnabled = new Button(grpSelectedSetting, SWT.CHECK);
		chkEnabled.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		chkEnabled.setText("Enabled");
		
        spacerLabel3 = new Label(grpSelectedSetting, SWT.NONE);
        spacerLabel3.setText(" ");

		chkInvalid = new Button(grpSelectedSetting, SWT.CHECK);
		chkInvalid.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		chkInvalid.setText("Suspend data collection if invalid");
		
        btnRestoreSingle = new Button(grpSelectedSetting, SWT.NONE);
        btnRestoreSingle.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
        btnRestoreSingle.setText("Restore Configuration Values");
        btnRestoreSingle.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                viewModel.resetFromSource();
            }
        });
        btnRestoreSingle.setEnabled(false);
        
		btnSend = new Button(grpSelectedSetting, SWT.NONE);
		btnSend.setText("Apply Changes");
        btnSend.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 3, 1));
        btnSend.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	dialog.setMessage(CONFIRM_CHANGES_HINT, IMessageProvider.INFORMATION);
                viewModel.sendChanges();
            }
        });
		parent.getShell().setDefaultButton(btnSend);

        grpGlobalSettings = new Group(this, SWT.NONE);
        grpGlobalSettings.setText("Global Settings");
        grpGlobalSettings.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
        grpGlobalSettings.setLayout(new GridLayout(1, false));

        btnRestoreAll = new Button(grpGlobalSettings, SWT.WRAP | SWT.PUSH);
        GridData gdBtnRestoreAll = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
        gdBtnRestoreAll.widthHint = 133;
        btnRestoreAll.setLayoutData(gdBtnRestoreAll);
        btnRestoreAll.setText("Restore All \n Configuration Values");
        btnRestoreAll.addSelectionListener(restoreAllConfigurationValues);
        
        grpAdditionalSection = new Group(this, SWT.NONE);
        grpAdditionalSection.setText("Additional");
        grpAdditionalSection.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
        grpAdditionalSection.setLayout(new GridLayout(2, false));
        
		btnDisplayBlockInfo = new Button(grpAdditionalSection,  SWT.WRAP | SWT.PUSH);
		GridData gdBtnBlockInfo = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
		gdBtnBlockInfo.widthHint = 100;
		btnDisplayBlockInfo.setLayoutData(gdBtnBlockInfo);
		btnDisplayBlockInfo.setText("Edit Host \n Configuration");
		btnDisplayBlockInfo.addSelectionListener(new SelectionAdapter() {
	        @Override
	        public void widgetSelected(SelectionEvent e) {
	        	new EditBlockHandler(currentBlock.getName()).execute(getShell());
	            }
	        }
		);
		btnDisplayBlockInfo.setEnabled(false);

        configServer.saveAs().addOnCanWriteChangeListener(canWriteListener);

        setModel(viewModel);
        setBlock(null);
        
        parent.addDisposeListener(e -> configServer.saveAs().removeOnCanWriteChangeListener(canWriteListener));
	}

    private void setModel(RunControlViewModel viewModel) {
    	DataBindingContext bindingContext = new DataBindingContext();
    	
        bindingContext.bindValue(WidgetProperties.enabled().observe(btnSend),
                BeanProperties.value("sendEnabled").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtLowLimit),
                BeanProperties.value(RunControlViewModel.LOW_LIMIT_BINDING_NAME).observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtHighLimit),
                BeanProperties.value(RunControlViewModel.HIGH_LIMIT_BINDING_NAME).observe(viewModel));
        bindingContext.bindValue(WidgetProperties.buttonSelection().observe(chkEnabled),
                BeanProperties.value(RunControlViewModel.ENABLED_BINDING_NAME).observe(viewModel));
        bindingContext.bindValue(WidgetProperties.buttonSelection().observe(chkInvalid),
                BeanProperties.value(RunControlViewModel.SUSPEND_ON_INVALID_BINDING_NAME).observe(viewModel));
    }
	
    private void setAllEnabled(boolean enabled) {
        txtLowLimit.setEnabled(enabled);
        txtHighLimit.setEnabled(enabled);
        chkEnabled.setEnabled(enabled);
        chkInvalid.setEnabled(enabled);
        btnRestoreSingle.setEnabled(enabled);
        viewModel.setSendEnabled(enabled);
        btnDisplayBlockInfo.setEnabled(enabled);
    }

    /**
     * Change the block that we are examining the run controls of.
     * 
     * @param block
     *            The new block to examine.
     */
	public void setBlock(DisplayBlock block) {
        viewModel.setSource(block);

		if (block == null) {
			name.setText("");
            setAllEnabled(false);
			return;
		}

        setAllEnabled(canSend);

		name.setText(block.getName());
		currentBlock = block;
	}

    private SelectionAdapter restoreAllConfigurationValues = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            if (MessageDialog.openConfirm(getShell(), RESET_ALL_DIALOG_TITLE, RESET_ALL_DIALOG_MESSAGE)) {
                viewModel.resetAllRunControlSettings();
                setBlock(null);
            }
        }
    };
}
