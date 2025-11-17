/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2025 Science & Technology Facilities Council.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.configuration.GlobalMacro;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;

/**
 * Panel showing an overview of all global macros.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class GlobalMacroOverviewPanel extends Composite {
	private Collection<GlobalMacroViewModel> macros;
	private final GlobalMacroTable table; 

    /**
     * Constructor for the Macro panel.
     * 
     * @param parent The parent composite.
     * @param style The SWT style.
     * @param config The configuration details.
     */
	public GlobalMacroOverviewPanel(Composite parent, int style, EditableConfiguration config) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		table = new GlobalMacroTable(this, SWT.NONE, SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
        GridData grid = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        grid.widthHint = 428;
        table.setLayoutData(grid);
        setMacros(config.getGlobalmacros());
        table.setRows(macros);
	}
	
    /**
     * Sets the macros to be displayed by the panel.
     * 
     * @param globalmacros The macros that are defined at the global level.
     */
	public void setMacros(final List<GlobalMacro> globalmacros) {
		macros = (globalmacros == null) ? new ArrayList<>()
				: globalmacros.stream().sorted().filter(Objects::nonNull)
						.flatMap(macro -> macro.getMacros().entrySet().stream().sorted(Map.Entry.comparingByKey()).map(
								entry -> new GlobalMacroViewModel(macro.getName(), entry.getKey(), entry.getValue())))
						.collect(Collectors.toList());
	}
}
