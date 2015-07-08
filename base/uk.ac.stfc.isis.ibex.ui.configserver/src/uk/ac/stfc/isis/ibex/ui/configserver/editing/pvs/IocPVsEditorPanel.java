
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.pvs;

import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.util.HashSet;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;

import uk.ac.stfc.isis.ibex.configserver.configuration.PVDefaultValue;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MessageDisplayer;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.IIocDependentPanel;

public class IocPVsEditorPanel extends Composite implements IIocDependentPanel {
	private IocPVsTable iocPVsTable;
	private IocPVDetailsPanel details;
	private EditableIoc ioc;
	private Button btnAdd;
	private final String newPVName = "NEW_PV";
	
	public IocPVsEditorPanel(Composite parent, int style, final MessageDisplayer messageDisplayer) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		iocPVsTable = new IocPVsTable(this, SWT.NONE, 0);
		GridData gd_iocPVsTable = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_iocPVsTable.heightHint = 200;
		iocPVsTable.setLayoutData(gd_iocPVsTable);
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(2, false));
		
		btnAdd = new Button(composite, SWT.NONE);
		GridData gd_btnAdd = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gd_btnAdd.widthHint = 70;
		btnAdd.setLayoutData(gd_btnAdd);
		btnAdd.setText("Add");
		btnAdd.setEnabled(false);
		
		final Button btnRemove = new Button(composite, SWT.NONE);
		GridData gd_btnRemove = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnRemove.widthHint = 70;
		btnRemove.setLayoutData(gd_btnRemove);
		btnRemove.setText("Remove");
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				ioc.getPvs().remove(iocPVsTable.firstSelectedRow());
				iocPVsTable.setRows(ioc.getPvs());
			}
		});
		btnRemove.setEnabled(false);
		
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				PVDefaultValue selected = new PVDefaultValue(generateNewName(), "NEW_VALUE");
				ioc.getPvs().add(selected);
				iocPVsTable.setRows(ioc.getPvs());
				iocPVsTable.setSelection(ioc.getPvs().size()-1);
				
				// Why is this not happening automatically
				btnRemove.setEnabled(true);
				details.setPV(selected, ioc);
			}
		});

		details = new IocPVDetailsPanel(this, SWT.NONE, messageDisplayer);
		Composite detailsComposite = (Composite)details;
		detailsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		iocPVsTable.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				PVDefaultValue selected = iocPVsTable.firstSelectedRow();
				btnRemove.setEnabled(selected != null);
				details.setPV(selected, ioc);
			}
		});
		details.setEnabled(false);
	}
	
	private String generateNewName() {
		HashSet<String> names = new HashSet<String>();
		for (PVDefaultValue pv : ioc.getPvs()) {
			names.add(pv.getName());
		}
		String name;
		int i=0;
		do {
			name = newPVName;
			if ( i>0 ) {
				name = name + Integer.toString(i);
			}
			i++;
		}
		while ( names.contains(name));
		
		return name;
	}

	@Override
	public CompositeContext createContext(ColorModel arg0, ColorModel arg1,
			RenderingHints arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIoc(EditableIoc ioc) {
		this.ioc = ioc;
		iocPVsTable.setRows(ioc!=null ? ioc.getPvs() : null);
		boolean enabled = ioc!=null && ioc.isEditable();
		setEnabled(enabled);
		btnAdd.setEnabled(enabled);
		details.setEnabled(enabled);
		details.setPVs(ioc.getAvailablePVs());
	}
}
