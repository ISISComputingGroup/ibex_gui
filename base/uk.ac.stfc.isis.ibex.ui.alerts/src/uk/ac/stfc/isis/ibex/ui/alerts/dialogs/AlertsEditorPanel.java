/*
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

package uk.ac.stfc.isis.ibex.ui.alerts.dialogs;

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
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayAlerts;
import uk.ac.stfc.isis.ibex.epics.writing.OnCanWriteChangeListener;
import uk.ac.stfc.isis.ibex.ui.alerts.AlertsViewModel;

/**
 * A panel to edit the alerts control settings for the selected block.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class AlertsEditorPanel extends Composite {
    private static final String RESET_ALL_DIALOG_TITLE = "Confirm Restore";
    private static final String RESET_ALL_DIALOG_MESSAGE = "Are you sure you want to restore all alerts settings to their configuration values?";
	private static final String CONFIRM_CHANGES_HINT = "WARNING! The settings applied here are not permanent and will be overriden if current configuration changes (see: User Manual - Run Controls)";
	private final Label name;
	private final Text txtLowLimit;
	private final Text txtHighLimit;
	private final Text txtDelayIn;
	private final Text txtDelayOut;
	private final Button chkEnabled;

	private final Button btnSend;
    private final Button btnRestoreSingle;
    private Button btnRestoreAll;
    private Group grpGlobalSettings;
    private boolean canSend;

    private final AlertsViewModel viewModel;
	
	private OnCanWriteChangeListener canWriteListener = canWrite -> {
        canSend = canWrite;
        btnRestoreAll.setEnabled(canWrite);
	};

	/**
	 * Creates the panel for editing the run control on one block.
	 * 
	 * @param parent       The parent composite.
	 * @param style        The SWT style of the panel.
	 * @param configServer The config server object used to write to the instrument.
	 * @param viewModel    The view model for this panel.
	 * @param dialog       The initial dialog that opened this panel.
	 * 
	 */
    public AlertsEditorPanel(EditAlertsDialog dialog, Composite parent, int style, ConfigServer configServer,
            final AlertsViewModel viewModel) {
		super(parent, style);
        this.viewModel = viewModel;

        setLayout(new GridLayout(3, false));

		Group grpSelectedSetting = new Group(this, SWT.NONE);
        grpSelectedSetting.setText("Alert Settings");
        grpSelectedSetting.setLayout(new GridLayout(10, false));
		
		Label lblName = new Label(grpSelectedSetting, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");
		
		name = new Label(grpSelectedSetting, SWT.NONE);
        GridData gdLblName = new GridData(SWT.FILL, SWT.CENTER, false, false, 9, 1);
		gdLblName.widthHint = 150;
		name.setLayoutData(gdLblName);
		
		addLabel(grpSelectedSetting, "Low Limit:");
		txtLowLimit = new Text(grpSelectedSetting, SWT.BORDER);
		addLayout(txtLowLimit);
		addLabel(grpSelectedSetting, "High Limit:");
		txtHighLimit = new Text(grpSelectedSetting, SWT.BORDER);
		addLayout(txtHighLimit);
		
		chkEnabled = new Button(grpSelectedSetting, SWT.CHECK);
		chkEnabled.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		chkEnabled.setText("Enabled");
		
		addLabel(grpSelectedSetting, "Delay In:");
		txtDelayIn = new Text(grpSelectedSetting, SWT.BORDER);
		addLayout(txtDelayIn);
		addLabel(grpSelectedSetting, "Delay Out:");
		txtDelayOut = new Text(grpSelectedSetting, SWT.BORDER);
		addLayout(txtDelayOut);

        btnRestoreSingle = new Button(grpSelectedSetting, SWT.NONE);
        btnRestoreSingle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 8, 1));
        btnRestoreSingle.setText("Reset Values");
        btnRestoreSingle.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                viewModel.resetFromSource();
            }
        });
        btnRestoreSingle.setEnabled(false);
        
		btnSend = new Button(grpSelectedSetting, SWT.NONE);
		btnSend.setText("Apply Changes");
        btnSend.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
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
        
        configServer.saveAs().addOnCanWriteChangeListener(canWriteListener);
        setModel(viewModel);
        setAlertDetails(null);
        
        parent.addDisposeListener(e -> configServer.saveAs().removeOnCanWriteChangeListener(canWriteListener));
	}

	/**
	 * @param field the text field to add layout data to
	 */
	private void addLayout(final Text field) {
		GridData gdTxtLow = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
        gdTxtLow.widthHint = 50;
        field.setLayoutData(gdTxtLow);
	}

	/**
	 * @param grpSelectedSetting the parent group for the label
	 * @param label the text of the label
	 */
	private void addLabel(Group grpSelectedSetting, String label) {
		Label lblLowLimit = new Label(grpSelectedSetting, SWT.NONE);
		lblLowLimit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLowLimit.setText(label);
	}

    private void setModel(AlertsViewModel viewModel) {
    	DataBindingContext bindingContext = new DataBindingContext();
    	
        bindingContext.bindValue(WidgetProperties.enabled().observe(btnSend),
                BeanProperties.value("sendEnabled").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtLowLimit),
                BeanProperties.value(AlertsViewModel.LOW_LIMIT_BINDING_NAME).observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtHighLimit),
                BeanProperties.value(AlertsViewModel.HIGH_LIMIT_BINDING_NAME).observe(viewModel));
        bindingContext.bindValue(WidgetProperties.buttonSelection().observe(chkEnabled),
                BeanProperties.value(AlertsViewModel.ENABLED_BINDING_NAME).observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtDelayIn),
                BeanProperties.value(AlertsViewModel.DELAYIN_BINDING_NAME).observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtDelayOut),
                BeanProperties.value(AlertsViewModel.DELAYOUT_BINDING_NAME).observe(viewModel));
    }
	
    private void setAllEnabled(boolean enabled) {
        txtLowLimit.setEnabled(enabled);
        txtHighLimit.setEnabled(enabled);
        txtDelayIn.setEnabled(enabled);
        txtDelayOut.setEnabled(enabled);
        chkEnabled.setEnabled(enabled);
        btnRestoreSingle.setEnabled(enabled);
        viewModel.setSendEnabled(enabled);
    }

    /**
     * Change the details that we are examining the alert controls of.
     * 
     * @param alert
     *            The new block to examine.
     */
	public void setAlertDetails(DisplayAlerts alert) {
        viewModel.setSource(alert);

		if (alert == null) {
			name.setText("");
            setAllEnabled(false);
			return;
		}
        setAllEnabled(canSend);
		name.setText(alert.getName());
	}

    private SelectionAdapter restoreAllConfigurationValues = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            if (MessageDialog.openConfirm(getShell(), RESET_ALL_DIALOG_TITLE, RESET_ALL_DIALOG_MESSAGE)) {
                viewModel.resetAlertSettings();
                setAlertDetails(null);
            }
        }
    };
}
