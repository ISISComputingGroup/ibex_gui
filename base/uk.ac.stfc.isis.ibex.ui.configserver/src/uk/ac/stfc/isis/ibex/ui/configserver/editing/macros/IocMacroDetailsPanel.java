
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2019 Science & Technology Facilities Council.
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
import java.util.stream.Collectors;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

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
	private MacroValueValidator valueValidator;
	private Label macroValueErrorLabel;
	
	private Label errorIconLabel;
    private Image scaled;
    
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
		setLayout(new GridLayout(1, false));
		
        displayMacrosTable = new MacroTable(this, SWT.NONE, SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
        GridData gdAvailableMacrosTable = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gdAvailableMacrosTable.widthHint = 428;
        displayMacrosTable.setLayoutData(gdAvailableMacrosTable);
        displayMacrosTable.addSelectionChangedListener(arg0 -> setSelectedMacro(displayMacrosTable.firstSelectedRow()));

        Composite cmpSelectedPv = new Composite(this, SWT.NONE);
        cmpSelectedPv.setLayout(new GridLayout(3, false));
        cmpSelectedPv.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        errorIconLabel = new Label(cmpSelectedPv, SWT.NONE);
        errorIconLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        errorIconLabel.setText("");

        Display display = Display.getCurrent();
        Image img = display.getSystemImage(SWT.ICON_WARNING);
        scaled = new Image(display, img.getImageData().scaledTo(20, 20));

        errorIconLabel.setImage(scaled);

        macroValueErrorLabel = new Label(cmpSelectedPv, SWT.NONE);
        GridData gdMacroValueErrorLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
        gdMacroValueErrorLabel.widthHint = 300;
        macroValueErrorLabel.setLayoutData(gdMacroValueErrorLabel);
        macroValueErrorLabel.setText("placeholder placeholder placeholder placeholder");

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
		displayMacrosTable.setCanEdit(canEdit);
		
        DataBindingContext bindingContext = new DataBindingContext();

		valueValidator = new MacroValueValidator(null, macroValueErrorLabel);
		
		valueValidator.validate("");
		displayMacrosTable.setValidator(valueValidator);
		
		bindingContext.bindValue(WidgetProperties.visible().observe(errorIconLabel), 
				BeanProperties.value(MacroValueValidator.SHOW_WARNING_ICON).observe(valueValidator));
		
		displayMacrosTable.setRows(macros.stream().map(macro -> new MacroViewModel(macro)).collect(Collectors.toList()));
		displayMacrosTable.deselectAll();
	}
	
	/**
	 * Set a new selected macro.
	 * 
	 * @param macroViewModel The macro to set
	 */
	public void setSelectedMacro(MacroViewModel macroViewModel) {			
		valueValidator.setMacro(macroViewModel);
	}

    @Override
    public void dispose() {
        super.dispose();
        scaled.dispose();
    }
}
