
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

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlServer;
import uk.ac.stfc.isis.ibex.ui.runcontrol.RunControlViewModel;
import uk.ac.stfc.isis.ibex.ui.runcontrol.commands.RunControlHandler;

/**
 * UI elements for editing run control settings, as used by run control dialog.
 * Contains a table of blocks, a RunControlEditorPanel and a reset all blocks
 * button.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class RunControlSettingsPanel extends Composite {
	private final RunControlSettingsTable table;
	private final RunControlEditorPanel editor;
	private final ConfigServer configServer;
    private UpdatedValue<Configuration> config;

	private PropertyChangeListener updateTable = new PropertyChangeListener() {
		@Override
		public void propertyChange(final PropertyChangeEvent arg0) {
            Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					setBlocks();
				}
			});
		}
	};

    /**
     * The constructor.
     * 
     * @param parent the parent composite
     * @param style the SWT style
     * @param configServer the configuration server
     * @param runControlServer the runcontrol server
     * @param viewModel the view model
     * @param dialog The initial dialog that opened this panel
     */
	public RunControlSettingsPanel(EditRunControlDialog dialog, Composite parent, int style, ConfigServer configServer, RunControlServer runControlServer,
			RunControlViewModel viewModel) {
		super(parent, style);
		
		this.configServer = configServer;
        config = new UpdatedObservableAdapter<Configuration>(this.configServer.currentConfig());
        
        setLayout(new GridLayout(2, false));

        table = new RunControlSettingsTable(this, SWT.NONE, SWT.FULL_SELECTION | SWT.BORDER);
        GridData gdTable = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gdTable.heightHint = 200;
		table.setLayoutData(gdTable);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
        editor = new RunControlEditorPanel(dialog, this, SWT.NONE, this.configServer, viewModel);
        
        config.addPropertyChangeListener(updateTable, true);
        table.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent arg0) {
                editor.setBlock(table.firstSelectedRow());
            }
        });
	}

	private void setBlocks() {
		final Collection<DisplayBlock> settings = Configurations.getInstance().display().getDisplayBlocks();
		if (settings != null) {
			table.setRows(settings);
		}
	}

}
