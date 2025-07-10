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
	private final Label name;
	private final Text txtLowLimit;
	private final Text txtHighLimit;
	private final Text txtDelayIn;
	private final Text txtDelayOut;
	private final Button chkEnabled;
	private final Text txtMessage;
	private final Text txtEmails;
	private final Text txtMobiles;

	private final Button btnSend;
    private final Button btnResetAlert;
    private Button btnResetAlertTopLevel;
    private final Button btnApplyTopLevel;
    private boolean canSend;

    private final AlertsViewModel viewModel;
	
	private OnCanWriteChangeListener canWriteListener = canWrite -> {
        canSend = canWrite;
        btnResetAlertTopLevel.setEnabled(canWrite);
	};

	/**
	 * Creates the panel for editing the run control on one block.
	 * 
	 * @param parent       The parent composite.
	 * @param style        The SWT style of the panel.
	 * @param configServer The config server object used to write to the instrument.
	 * @param viewModel    The view model for this panel.
	 * @param dialog       The initial dialog that opened this panel.
	 */
    public AlertsEditorPanel(EditAlertsDialog dialog, Composite parent, int style, ConfigServer configServer,
            final AlertsViewModel viewModel) {
		super(parent, style);
        this.viewModel = viewModel;

        setLayout(new GridLayout(3, false));

		Group grpSelectedSetting = new Group(this, SWT.NONE);
        grpSelectedSetting.setText("Block-level Alert Settings");
        grpSelectedSetting.setLayout(new GridLayout(4, false));
		
		Label lblName = new Label(grpSelectedSetting, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");
		
		name = new Label(grpSelectedSetting, SWT.NONE);
        GridData gdLblName = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gdLblName.widthHint = 150;
		name.setLayoutData(gdLblName);
		chkEnabled = new Button(grpSelectedSetting, SWT.CHECK);
		chkEnabled.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		chkEnabled.setText("Enabled");
		
		addLabel(grpSelectedSetting, "Low Limit:");
		txtLowLimit = new Text(grpSelectedSetting, SWT.BORDER);
		addLayout(txtLowLimit, 50);
		addLabel(grpSelectedSetting, "High Limit:");
		txtHighLimit = new Text(grpSelectedSetting, SWT.BORDER);
		addLayout(txtHighLimit, 50);
		
		addLabel(grpSelectedSetting, "Delay In:");
		txtDelayIn = new Text(grpSelectedSetting, SWT.BORDER);
		addLayout(txtDelayIn, 50);
		addLabel(grpSelectedSetting, "Delay Out:");
		txtDelayOut = new Text(grpSelectedSetting, SWT.BORDER);
		addLayout(txtDelayOut, 50);

        btnResetAlert = new Button(grpSelectedSetting, SWT.NONE);
        btnResetAlert.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
        btnResetAlert.setText("Reset Values");
        btnResetAlert.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                viewModel.resetBlockLevelSettings();
            }
        });
        btnResetAlert.setEnabled(false);
        
		btnSend = new Button(grpSelectedSetting, SWT.NONE);
		btnSend.setText("Apply Changes");
        btnSend.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
        btnSend.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
               viewModel.applyBlockLevelChanges();
            }
        });
		parent.getShell().setDefaultButton(btnSend);

		Group grpGlobalSettings = new Group(this, SWT.NONE);
        grpGlobalSettings.setText("Global Alert Settings");
        grpGlobalSettings.setLayout(new GridLayout(2, false));
        addLabel(grpGlobalSettings, "Message:");
        txtMessage = new Text(grpGlobalSettings, SWT.BORDER);
		addLayout(txtMessage, 150);
		txtMessage.setToolTipText("The message to send when the alert is triggered");
        addLabel(grpGlobalSettings, "Emails(comma separated):");
        txtEmails = new Text(grpGlobalSettings, SWT.BORDER);
        txtEmails.setToolTipText("Emails to notify when the alert is triggered");
		addLayout(txtEmails, 150);
        addLabel(grpGlobalSettings, "Mobiles(comma separated):");
        txtMobiles = new Text(grpGlobalSettings, SWT.BORDER);
        txtMobiles.setToolTipText("Mobile numbers to text when the alert is triggered");
		addLayout(txtMobiles, 150);
		btnResetAlertTopLevel = new Button(grpGlobalSettings, SWT.NONE);
		btnResetAlertTopLevel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnResetAlertTopLevel.setText("Reset Values");
		btnResetAlertTopLevel.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                viewModel.resetTopLevelSettings();
            }
        });
		btnResetAlertTopLevel.setEnabled(false);
		
		btnApplyTopLevel = new Button(grpGlobalSettings, SWT.NONE);
		btnApplyTopLevel.setText("Apply Changes");
		btnApplyTopLevel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnApplyTopLevel.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
               viewModel.applyTopLevelChanges();
            }
        });
        configServer.saveAs().addOnCanWriteChangeListener(canWriteListener);
        setModel(viewModel);
        setAlertDetails(null);
        
        parent.addDisposeListener(e -> configServer.saveAs().removeOnCanWriteChangeListener(canWriteListener));
	}

	/**
	 * @param field the text field to add layout data to
	 */
	private void addLayout(final Text field, int width) {
		GridData textField = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
        textField.widthHint = width;
        field.setLayoutData(textField);
	}

	/**
	 * @param grpSelectedSetting the parent group for the label
	 * @param label the text of the label
	 */
	private void addLabel(Group grpSelectedSetting, String label) {
		Label newLabel = new Label(grpSelectedSetting, SWT.NONE);
		newLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		newLabel.setText(label);
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
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtMessage),
                BeanProperties.value(AlertsViewModel.MESSAGE_BINDING_NAME).observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtEmails),
                BeanProperties.value(AlertsViewModel.EMAILS_BINDING_NAME).observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtMobiles),
                BeanProperties.value(AlertsViewModel.MOBILES_BINDING_NAME).observe(viewModel));
    }
	
    private void setAllEnabled(boolean enabled) {
        txtLowLimit.setEnabled(enabled);
        txtHighLimit.setEnabled(enabled);
        txtDelayIn.setEnabled(enabled);
        txtDelayOut.setEnabled(enabled);
        chkEnabled.setEnabled(enabled);
        btnResetAlert.setEnabled(enabled);
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
            viewModel.resetTopLevelSettings();
			return;
		}
        setAllEnabled(canSend);
		name.setText(alert.getName());
	}
}
