
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
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
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
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWriter;
import uk.ac.stfc.isis.ibex.ui.runcontrol.RunControlViewModel;

/**
 * A panel to edit the run control settings for the selected block.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class RunControlEditorPanel extends Composite {
    private static final String RESET_ALL_DIALOG_TITLE = "Confirm Run-Control Restore";
    private static final String RESET_ALL_DIALOG_MESSAGE = "Are you sure you want to restore all run-control settings to their configuration values?";
    private final ConfigServer configServer;
	private final Label name;
	private final Text txtLowLimit;
	private final Text txtHighLimit;
	private final Button chkEnabled;
	private final Button btnSend;
    private final Button btnRestoreSingle;
    private final Button btnRestoreAll;
    private final Label spacerLabel;
    private final Label spacerLabel2;
    private Group grpGlobalSettings;
	private DisplayBlock block;
    private boolean canSend;

    private final RunControlViewModel viewModel;
	
    private Subscription saveAsSubscription;

	/**
	 * Disable the send button if does not have permission to edit configs.
	 */
    protected final SameTypeWriter<Configuration> configService = new SameTypeWriter<Configuration>() {
		@Override
		public void onCanWriteChanged(boolean canWrite) {
            canSend = canWrite;
            btnRestoreAll.setEnabled(canWrite);
		};	
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
     */
    public RunControlEditorPanel(Composite parent, int style, ConfigServer configServer,
            final RunControlViewModel viewModel) {
		super(parent, style);
		
		this.configServer = configServer;
        this.viewModel = viewModel;

        setLayout(new GridLayout(2, false));

		Group grpSelectedSetting = new Group(this, SWT.NONE);
        grpSelectedSetting.setText("Block Settings");
        grpSelectedSetting.setLayout(new GridLayout(7, false));
		
		Label lblName = new Label(grpSelectedSetting, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");
		
		name = new Label(grpSelectedSetting, SWT.NONE);
        GridData gdLblName = new GridData(SWT.FILL, SWT.CENTER, false, false, 6, 1);
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
		
        btnRestoreSingle = new Button(grpSelectedSetting, SWT.NONE);
        btnRestoreSingle.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
        btnRestoreSingle.setText("Restore Configuration Values");
        btnRestoreSingle.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                viewModel.resetCurrentBlock();
            }
        });
        btnRestoreSingle.setEnabled(false);

		btnSend = new Button(grpSelectedSetting, SWT.NONE);
		btnSend.setText("Apply Changes");
        btnSend.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 3, 1));
        btnSend.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
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

        // A bit of a work-around to see if we have write permissions
        // by seeing if we are able to edit the config.
        saveAsSubscription = this.configServer.saveAs().subscribe(configService);

        setModel(viewModel);
        setBlock(null);
	}
    
    @Override
    public void dispose() {
        // Must dispose of the subscription otherwise can get multiple
        // subscriptions when swapping instruments
        saveAsSubscription.removeObserver();
        super.dispose();
    }

    private void setModel(RunControlViewModel viewModel) {
    	DataBindingContext bindingContext = new DataBindingContext();
    	
        bindingContext.bindValue(WidgetProperties.enabled().observe(btnSend),
                BeanProperties.value("sendEnabled").observe(viewModel));
    	
        bindingContext.bindValue(SWTObservables.observeText(txtLowLimit, SWT.Modify),
                BeanProperties.value("txtLowLimit").observe(viewModel));
        bindingContext.bindValue(SWTObservables.observeText(txtHighLimit, SWT.Modify),
                BeanProperties.value("txtHighLimit").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.selection().observe(chkEnabled),
                BeanProperties.value("rcEnabled").observe(viewModel));
    }
	
    private void setAllEnabled(boolean enabled) {
        txtLowLimit.setEnabled(enabled);
        txtHighLimit.setEnabled(enabled);
        chkEnabled.setEnabled(enabled);
        btnRestoreSingle.setEnabled(enabled);
        viewModel.setSendEnabled(enabled);
    }

    /**
     * Change the block that we are examining the run controls of.
     * 
     * @param block
     *            The new block to examine.
     */
	public void setBlock(DisplayBlock block) {
		this.block = block;

        viewModel.setBlock(block);

		if (block == null) {
			name.setText("");
            setAllEnabled(false);
			return;
		}

        setAllEnabled(canSend);

		name.setText(block.getName());
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
