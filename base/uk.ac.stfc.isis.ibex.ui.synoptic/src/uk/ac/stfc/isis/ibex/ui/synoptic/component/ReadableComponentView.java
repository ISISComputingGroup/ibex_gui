
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

package uk.ac.stfc.isis.ibex.ui.synoptic.component;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.synoptic.model.ReadableComponentProperty;

/**
 * A View for a readable component.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class ReadableComponentView extends Composite {
    
    private static final Color GREY = SWTResourceManager.getColor(240, 240, 240);
    private static final int BORDER_WIDTH = 2;
	
	private Label propertyName;
	private StyledText value;
	private Composite valueContainer;
	
	private final DataBindingContext bindingContext = new DataBindingContext();

	/**
	 * Display the readable component.
	 * @param parent the parent
	 * @param property the readable component property
	 */
	public ReadableComponentView(Composite parent, ReadableComponentProperty property) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.VERTICAL));
		
		propertyName = new Label(this, SWT.NONE);		
		propertyName.setAlignment(SWT.CENTER);
		propertyName.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		propertyName.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		
		// Additional container to hold the text to draw a border around it
        valueContainer = new Composite(this, SWT.CENTER);
        GridLayout valueContainerLayout = new GridLayout(1, false);
        valueContainerLayout.marginWidth = BORDER_WIDTH;
        valueContainerLayout.marginHeight = BORDER_WIDTH;
        valueContainerLayout.verticalSpacing = 0;
        valueContainerLayout.horizontalSpacing = 0;
        valueContainer.setLayout(valueContainerLayout);
        
        value = new StyledText(valueContainer, SWT.RIGHT);
        GridData gdValue = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        value.setBackground(GREY);
        value.setLayoutData(gdValue);
        value.setDoubleClickEnabled(false);
        value.setEditable(false);
        
        value.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL));
        value.setAlignment(SWT.RIGHT);
        valueContainer.pack();
		
        this.setTabList(new Control[0]); // Remove tabbing as read only

		setProperty(property);
	}

	private void setProperty(ReadableComponentProperty property) {
		propertyName.setText(property.displayName());
		bindingContext.bindValue(WidgetProperties.text().observe(value), BeanProperties.value("value").observe(property.getValue()));
		
		UpdateValueStrategy borderStrategy = new UpdateValueStrategy();
        borderStrategy.setConverter(new PvStatusBorderColourConverter());

        bindingContext.bindValue(WidgetProperties.background().observe(valueContainer),
                BeanProperties.value("pvState").observe(property), null, borderStrategy);
	}
}
