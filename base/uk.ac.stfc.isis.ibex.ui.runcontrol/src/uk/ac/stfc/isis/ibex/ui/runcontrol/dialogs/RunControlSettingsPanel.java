
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlServer;
import uk.ac.stfc.isis.ibex.ui.runcontrol.RunControlViewModel;

@SuppressWarnings({"checkstyle:magicnumber"})
public class RunControlSettingsPanel extends Composite {

    private final static String RESET_ALL_DIALOG_TITLE = "Confirm run-control restore";
    private final static String RESET_ALL_DIALOG_MESSAGE = "Are you sure you want to restore all run-control settings to their configuration values?";

	private final Display display = Display.getDefault();
	private RunControlSettingsTable table;
	private RunControlEditorPanel editor;
	private final ConfigServer configServer;
	private final RunControlServer runControlServer;
    UpdatedValue<Configuration> config;

    private final RunControlViewModel runControlViewModel;

	private PropertyChangeListener updateTable = new PropertyChangeListener() {
		@Override
		public void propertyChange(final PropertyChangeEvent arg0) {
			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					setBlocks();
				}
			});
		}
	};

    private SelectionAdapter restoreAllConfigurationValues = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            if (MessageDialog.openConfirm(getShell(), RESET_ALL_DIALOG_TITLE, RESET_ALL_DIALOG_MESSAGE)) {
                runControlViewModel.resetRunControlSettings();
            }
        }
    };
    private Group grpGlobalSettings;

	public RunControlSettingsPanel(Composite parent, int style, ConfigServer configServer, RunControlServer runControlServer) {
		super(parent, style);
		
		this.configServer = configServer;
        config = new UpdatedObservableAdapter<Configuration>(this.configServer.currentConfig());
        config.addPropertyChangeListener(updateTable, true);
		
		this.runControlServer = runControlServer;
        this.runControlViewModel = new RunControlViewModel(configServer, runControlServer);
		
        setLayout(new GridLayout(2, false));

        table = new RunControlSettingsTable(this, SWT.NONE,
                SWT.V_SCROLL | SWT.NO_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        GridData gdTable = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gdTable.heightHint = 200;
		table.setLayoutData(gdTable);
		table.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent arg0) {
                editor.setBlock(table.firstSelectedRow());
            }
        });
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
        editor = new RunControlEditorPanel(this, SWT.NONE, this.configServer, this.runControlServer,
                this.runControlViewModel);

        grpGlobalSettings = new Group(this, SWT.NONE);
        grpGlobalSettings.setText("Global Settings");
        grpGlobalSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        grpGlobalSettings.setLayout(new GridLayout(1, false));

        Button btnNewButton = new Button(grpGlobalSettings, SWT.WRAP | SWT.PUSH);
        GridData gd_btnNewButton = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
        gd_btnNewButton.widthHint = 133;
        gd_btnNewButton.heightHint = 36;
        btnNewButton.setLayoutData(gd_btnNewButton);
        btnNewButton.setText("Restore All \n Configuration Values");
        btnNewButton.addSelectionListener(restoreAllConfigurationValues);
	}

	private void setBlocks() {
		Collection<DisplayBlock> settings = Configurations.getInstance().display().getDisplayBlocks();

		if (settings != null) {
			table.setRows(settings);
		}
	}

}
