
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;

@SuppressWarnings("checkstyle:magicnumber")
public class IocsEditorPanel extends Composite {

	private EditableIocsTable table;
	
	private final Display display = Display.getCurrent();
	private EditableConfiguration config;
	
    private static final int BUTTON_WIDTH = 100;

	private final PropertyChangeListener updateIocs = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (config != null) {
				updateIocs(config.getEditableIocs());
			}
		}
	};
	
	public IocsEditorPanel(Composite parent, int style, MessageDisplayer msgDisp) {
		super(parent, style);
        setLayout(new GridLayout(4, false));
		
        // IOC selection table
		table = new EditableIocsTable(this, SWT.NONE, SWT.FULL_SELECTION);
        GridData gdTable = new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1);
		gdTable.heightHint = 200;
		table.setLayoutData(gdTable);
		
		
        // Add IOC button
        Button btnAddIoc = new Button(this, SWT.NONE);
        btnAddIoc.setText("Add IOC");

        // Selected IOC readback
        Composite cmpSelectedIoc = new Composite(this, SWT.FILL);
        cmpSelectedIoc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        GridLayout glCmpSelectedIoc = new GridLayout(2, true);
//        glCmpSelectedIoc.marginWidth = 0;
//        glCmpSelectedIoc.marginHeight = 0;
        cmpSelectedIoc.setLayout(glCmpSelectedIoc);

        Label lblSelectedIoc = new Label(cmpSelectedIoc, SWT.NONE);
        lblSelectedIoc.setText("Selected:");
        lblSelectedIoc.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

        Text selectedIoc = new Text(cmpSelectedIoc, SWT.BORDER);
        selectedIoc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        selectedIoc.setEnabled(false);

        // Edit IOC button
        Button btnEditIoc = new Button(this, SWT.NONE);
        btnEditIoc.setText("Edit IOC");

        // Delete IOC Button
        Button btnDeleteIoc = new Button(this, SWT.NONE);
        btnDeleteIoc.setText("Delete IOC");

        GridData gdButton = new GridData();
        gdButton.widthHint = BUTTON_WIDTH;

        btnAddIoc.setLayoutData(gdButton);
        btnEditIoc.setLayoutData(gdButton);
        btnDeleteIoc.setLayoutData(gdButton);

        btnAddIoc.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
//                EditableIoc added = blockFactory.createNewBlock();
                EditIocDialog dialog = new EditIocDialog(getShell(), config, true);
                dialog.open();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                //
            }
        });
	}

	public void setConfig(EditableConfiguration config) {
		this.config = config;
		updateIocs(config.getEditableIocs());	
		config.addPropertyChangeListener(updateIocs);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		table.setEnabled(enabled);
	}
	
	private void updateIocs(final Collection<EditableIoc> iocs) {
		display.asyncExec(new Runnable() {	
			@Override
			public void run() {
				if (!table.isDisposed()) {
					table.setRows(iocs);
					table.refresh();	
				}
			}
		});
	}
}
