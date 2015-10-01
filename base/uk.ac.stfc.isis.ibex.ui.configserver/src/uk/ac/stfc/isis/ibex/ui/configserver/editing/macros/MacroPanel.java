
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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
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
	
	public MacroPanel(Composite parent, int style) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		details = new IocMacroDetailsPanel(this, SWT.NONE);
		details.setEnabled(false);
	}
	
	// Initialise the tab with macro data for an IOC
	public void setMacros(final Collection<Macro> setMacros, Collection<Macro> availableMacros, boolean canEdit) {
		this.canEditMacros = canEdit;
		this.displayMacros = makeDisplayMacroList(setMacros, availableMacros);
		
		displayMacros = sortMacroCollectionByName(displayMacros);
		details.setMacros(displayMacros, canEditMacros);

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
	
	/**
	 * Creates a list of macros to display, by combining available macros with the value of any macros set in
	 * set macros.
	 * 
	 * @param setMacros A collection containing the macros that have been set
	 * @param availableMacros A collection containing all the available macros
	 * @return
	 */
	private Collection<Macro> makeDisplayMacroList(Collection<Macro> setMacros, Collection<Macro> availableMacros) {
		displayMacros = new ArrayList<Macro>();
		
		for (Macro availableMacro : availableMacros) {
			boolean macroCurrentlySet = false;
			final Macro displayMacro = new Macro(availableMacro);
			for (final Macro setMacro : setMacros) {
				if (setMacro.getName().equals(availableMacro.getName())) {
					displayMacro.setValue(setMacro.getValue());
					displayMacro.addPropertyChangeListener("value", updateValueListener(setMacro, setMacros));
					macroCurrentlySet = true;
				}
			}

			displayMacros.add(displayMacro);
			
			if (!macroCurrentlySet) {
				displayMacro.addPropertyChangeListener("value", addSetMacroListener(displayMacro, setMacros));
			}
		}
		
		return displayMacros;
	}
	
	private PropertyChangeListener updateValueListener(final Macro setMacro, final Collection<Macro> setMacros) {
		return new PropertyChangeListener(){
			@Override
			public void propertyChange(PropertyChangeEvent newValue) {
				String updatedValue = (String) newValue.getNewValue();
				
				if (Strings.isNullOrEmpty(updatedValue)) {
					setMacros.remove(setMacro);
				} else {
					setMacro.setValue((String) newValue.getNewValue());
				}
			}
		};
	}
	
	private PropertyChangeListener addSetMacroListener(final Macro displayMacro, final Collection<Macro> setMacros) {
		return new PropertyChangeListener(){
			@Override
			public void propertyChange(PropertyChangeEvent newValue) {
				Macro newMacro = new Macro(displayMacro);
				displayMacro.setValue((String) newValue.getNewValue());
				setMacros.add(newMacro);
			}
		};
	}
}