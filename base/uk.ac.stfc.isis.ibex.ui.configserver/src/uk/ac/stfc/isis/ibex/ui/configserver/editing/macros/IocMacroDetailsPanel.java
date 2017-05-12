
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

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;

/**
 * This panel allows macro names and values to be set, and shows a list of available macros for an
 * IOC.
 * 
 * In the list of available macros a description and pattern is also shown. The macro can only be 
 * set if the pattern is matched by the new value.
 * 
 */
@SuppressWarnings("checkstyle:magicnumber")
public class IocMacroDetailsPanel extends Composite {
	private MacroTable displayMacrosTable;
	
    /**
     * Constructor for the macro details panel.
     * 
     * @param parent
     *            The parent composite.
     * @param style
     *            The SWT style.
     */
	public IocMacroDetailsPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
        Composite cmpTable = new Composite(this, SWT.NONE);
        cmpTable.setLayout(new GridLayout(1, false));

        displayMacrosTable = new MacroTable(cmpTable, SWT.NONE, SWT.FULL_SELECTION);
        GridData gdAvailableMacrosTable = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gdAvailableMacrosTable.widthHint = 428;
        displayMacrosTable.setLayoutData(gdAvailableMacrosTable);

	}
	
	/**
     * Called when changing IOCs. Should reset everything such as name,
     * selection etc.
     * 
     * @param macros
     *            The macros to display of the current IOC
     * @param canEdit
     *            If the IOC is editable
     */
	public void setMacros(Collection<Macro> macros, boolean canEdit) {
		displayMacrosTable.setRows(macros);
		displayMacrosTable.deselectAll();
		
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		displayMacrosTable.setEnabled(enabled);
	}
}
