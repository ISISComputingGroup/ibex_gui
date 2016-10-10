
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;

@SuppressWarnings("checkstyle:magicnumber")
public class IocsEditorPanel extends Composite {

	private IocsTable table;
	
	private final Display display = Display.getCurrent();
	private EditableConfiguration config;
	
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
		setLayout(new GridLayout(1, false));
		
		table = new IocsTable(this, SWT.NONE, SWT.FULL_SELECTION);
		GridData gdTable = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gdTable.heightHint = 200;
		table.setLayoutData(gdTable);
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
