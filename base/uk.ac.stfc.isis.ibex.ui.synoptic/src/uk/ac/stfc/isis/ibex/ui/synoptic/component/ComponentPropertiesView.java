
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

package uk.ac.stfc.isis.ibex.ui.synoptic.component;

import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;

import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.synoptic.model.ComponentProperty;
import uk.ac.stfc.isis.ibex.synoptic.model.ReadableComponentProperty;
import uk.ac.stfc.isis.ibex.synoptic.model.WritableComponentProperty;

public class ComponentPropertiesView extends Composite {
		
	public ComponentPropertiesView(Composite parent, Component component) {
		super(parent, SWT.BORDER);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 1;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		setLayout(gridLayout);
		
		createPropertyViews(component.properties());	
	}

	private void createPropertyViews(Set<ComponentProperty> properties) {
		String precedingPropertyName = null;
		for (ComponentProperty property : properties) {
			if (property instanceof WritableComponentProperty) {
				// Avoid duplication of property names
				boolean shouldDisplayPropertyName = !property.displayName().equals(precedingPropertyName); 
				createWriter((WritableComponentProperty) property, shouldDisplayPropertyName); 
			} else if (property instanceof ReadableComponentProperty) {
				createReader((ReadableComponentProperty) property); 
			}
			
			precedingPropertyName = property.displayName();
		}
	}

	private void createReader(ReadableComponentProperty property) {
		ReadableComponentView propertyView = new ReadableComponentView(this, property);
		setViewSize(propertyView);
	}

	private void createWriter(WritableComponentProperty property, boolean displayPropertyName) {
		WritableComponentView propertyView = new WritableComponentView(this, property, displayPropertyName);
		setViewSize(propertyView);
	}	

	private static void setViewSize(Composite view) {
		view.pack();
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd.minimumHeight = view.getSize().y;
		gd.heightHint = gd.minimumHeight; 
		gd.minimumWidth = 70;
		view.setLayoutData(gd);
	}
}
