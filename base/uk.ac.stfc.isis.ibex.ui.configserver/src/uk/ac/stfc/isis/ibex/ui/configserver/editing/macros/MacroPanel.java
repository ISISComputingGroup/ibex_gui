
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
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

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
	private Collection<Macro> displayMacros;
	private IocMacroDetailsPanel details;
	
	private boolean canEditMacros;
	
	public MacroPanel(Composite parent, int style, MessageDisplayer msgDisp) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		details = new IocMacroDetailsPanel(this, SWT.NONE, msgDisp);
		details.setEnabled(false);
	}
	
	// Initialise the tab with macro data for an IOC
	public void setMacros(Collection<Macro> setMacros, Collection<Macro> availableMacros, boolean canEdit) {
		this.displayMacros = new ArrayList<Macro>();
		this.canEditMacros = canEdit;
		
		for (Macro availableMacro : availableMacros) {
			Macro displayMacro = new Macro(availableMacro);
			for (Macro setMacro : setMacros) {
				if (setMacro.getName().equals(availableMacro.getName())) {
					displayMacro.setValue(setMacro.getValue());
				}
			}

			displayMacros.add(displayMacro);
		}
				
		setMacro(null);
	}
	
	// Set the macro to be edited
	private void setMacro(Macro macro) {
		displayMacros = sortMacroCollectionByName(displayMacros);
		details.setMacro(macro, displayMacros, canEditMacros);
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