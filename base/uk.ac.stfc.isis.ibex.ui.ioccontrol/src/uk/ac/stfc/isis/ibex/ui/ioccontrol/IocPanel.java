
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

package uk.ac.stfc.isis.ibex.ui.ioccontrol;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

import uk.ac.stfc.isis.ibex.configserver.EditableIocState;
import uk.ac.stfc.isis.ibex.configserver.IocControl;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IocTable;

public class IocPanel extends Composite {
	
	private final Display display = Display.getDefault();
	
	private IocTable table;
	private IocEditorPanel editor;
	private IocControl control;
	
	private PropertyChangeListener updateTable = new PropertyChangeListener() {	
		@Override
		public void propertyChange(PropertyChangeEvent arg0) {
			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					setIocs();
				}
			});
		}
	};
	
	public IocPanel(Composite parent, final IocControl control, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
				
		Composite container = new Composite(this, SWT.NONE);
		GridLayout glContainer = new GridLayout(1, false);
		glContainer.marginHeight = 0;
		glContainer.horizontalSpacing = 0;
		glContainer.marginWidth = 0;
		container.setLayout(glContainer);
		
		table = new IocTable(container, SWT.BORDER, SWT.V_SCROLL | SWT.NO_SCROLL | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		this.control = control;
		control.iocs().addPropertyChangeListener(updateTable, true);
		
		table.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				editor.setIoc(table.firstSelectedRow());
			}
		});

		editor = new IocEditorPanel(container, SWT.NONE, control);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
	}
	
	@Override
	public void dispose() {
		super.dispose();
		control.iocs().removePropertyChangeListener(updateTable);
	}

	private void setIocs() {		
		if (control.iocs().isSet()) {
			EditableIocState selected = table.firstSelectedRow();
			
			Collection<EditableIocState> rows = control.iocs().getValue();
			if (rows != null) {
				table.setRows(rows);
				resetLastSelectedIoc(selected, rows);
			}			
		}
	}

	private void resetLastSelectedIoc(EditableIocState lastSelected, Collection<EditableIocState> rows) {
		if (lastSelected == null) {
			return;
		}
		
		// Preserve selection if possible
		for (EditableIocState row : rows) {
			if (row.getName().equals(lastSelected.getName())) {
				editor.setIoc(row);
				return;
			}
		}
	}
}
