
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.macros;

import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MessageDisplayer;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.IIocDependentPanel;

/**
 * This panel shows the macros that have been set for a given IOC, and allows macros to be added
 * and removed.
 *
 */
public class MacroPanel extends Composite implements IIocDependentPanel {
	private MacroTable macroTable;
	private Collection<Macro> macros;
	private Collection<Macro> availableMacros;
	private Button btnAdd;
	private Button btnRemove;
	private Macro selected;
	private IocMacroDetailsPanel details;
	
	private boolean canEditMacros;
	
	public MacroPanel(Composite parent, int style, MessageDisplayer msgDisp) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Group macrosGroup = new Group(this, SWT.NONE);
		macrosGroup.setText("IOC Macros");
		macrosGroup.setLayout(new GridLayout(2, false));
		
		macroTable = new MacroTable(macrosGroup, SWT.NONE, SWT.FULL_SELECTION);
		macroTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		macroTable.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent arg0) {
				IStructuredSelection selection = (IStructuredSelection) arg0.getSelection();
				if (selection.size() > 0) {
					selected = (Macro)selection.getFirstElement();
					setMacro(selected);
					btnRemove.setEnabled(true);
				}
				else {
					btnRemove.setEnabled(false);
				}
			}
		});
		
		btnAdd = new Button(macrosGroup, SWT.NONE);
		GridData gd_btnAdd = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gd_btnAdd.widthHint = 70;
		btnAdd.setLayoutData(gd_btnAdd);
		btnAdd.setText("Add");
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (macros == null) return;

				Macro selected = new Macro(generateNewName(), "NEW_VALUE", "", "");
				macros.add(selected);
				macroTable.setRows(macros);
				macroTable.setSelection(macros.size() - 1);
				btnRemove.setEnabled(true);
				setMacro(selected);
			}
		});
		btnAdd.setEnabled(false);
		
		btnRemove = new Button(macrosGroup, SWT.NONE);
		GridData gd_btnRemove = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnRemove.widthHint = 70;
		btnRemove.setLayoutData(gd_btnRemove);
		btnRemove.setText("Remove");
		btnRemove.setEnabled(false);
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				macros.remove(macroTable.firstSelectedRow());
				setMacros(macros, availableMacros, canEditMacros);
			}
		});
		
		details = new IocMacroDetailsPanel(macrosGroup, SWT.NONE, msgDisp);
		Composite detailsComposite = (Composite)details;
		detailsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		details.setEnabled(false);
	}
	
	private String generateNewName() {
		HashSet<String> names = new HashSet<String>();
		for (Macro macro : macros) {
			names.add(macro.getName());
		}
		String name;
		int i = 0;
		do {
			name = "NEW_MACRO";
			if (i > 0) {
				name = name + Integer.toString(i);
			}
			i++;
		}
		while (names.contains(name));
		
		return name;
	}

	// Initialise the tab with macro data for an IOC
	public void setMacros(Collection<Macro> macros, Collection<Macro> availableMacros, boolean canEdit) {
		this.macros = macros;
		this.availableMacros = availableMacros;
		this.canEditMacros = canEdit;
		macroTable.setRows(macros);
		
		setMacro(null);
		btnAdd.setEnabled(canEdit);
	}
	
	// Set the macro to be edited
	private void setMacro(Macro macro) {
		availableMacros = sortMacroCollectionByName(availableMacros);
		details.setMacro(macro, macros, availableMacros, canEditMacros);
	}

	@Override
	public CompositeContext createContext(ColorModel arg0, ColorModel arg1,
			RenderingHints arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIoc(EditableIoc ioc) {
		setMacros(ioc.getMacros(), ioc.getAvailableMacros(), ioc.isEditable());
	}
	
	public static List<Macro> sortMacroCollectionByName(Collection<Macro> collection) {
		Comparator<Macro> comparator = new Comparator<Macro>(){
			public int compare(Macro macro1, Macro macro2) {
				return macro1.getName().compareTo(macro2.getName());
			}
		};
		
		List<Macro> sortedList = new ArrayList<Macro>(collection);
		Collections.sort(sortedList, comparator);
				
		return sortedList;
	}
}