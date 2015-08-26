
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.target;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.Property;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IPropertySelectionListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;

public class TargetPropertyDetailView extends Composite {
	
	private Label propertyLabel;
	private Composite propertyComposite;
	private Text key;
	private Text value;
	private SynopticViewModel model;

	private boolean updateLock;
	
	private final IPropertySelectionListener propertyListener = new IPropertySelectionListener() {		
		@Override
		public void selectionChanged(Property oldProperty, Property newProperty) {
			setProperty(newProperty);
		}
	};
	
	private final Listener propertyUpdateListener = new Listener() {
		@Override
		public void handleEvent(Event event) {
			if (!updateLock && model != null) {
				model.updateSelectedProperty(new Property(key.getText(), value.getText()));
			}			
		}
	};
		
	public TargetPropertyDetailView(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		propertyLabel = new Label(this, SWT.NONE);
		propertyLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		propertyLabel.setText("Select a property to view/edit details");
		
		propertyComposite = new Composite(this, SWT.NONE);
		propertyComposite.setLayout(new GridLayout(2, false));
		propertyComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblKey = new Label(propertyComposite, SWT.NONE);
		lblKey.setAlignment(SWT.RIGHT);
		lblKey.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblKey.setText("Key:");
		
		key = new Text(propertyComposite, SWT.BORDER);
		key.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		key.addListener(SWT.FocusOut, propertyUpdateListener);
		
		Label lblValue = new Label(propertyComposite, SWT.NONE);
		lblValue.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblValue.setText("Value:");
		lblValue.setAlignment(SWT.RIGHT);
		
		value = new Text(propertyComposite, SWT.BORDER);
		value.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		value.addListener(SWT.FocusOut, propertyUpdateListener);
	}
	
	public void setModel(SynopticViewModel model) {
		if (this.model != null) {
			model.removePropertySelectionListener(propertyListener);
		}
		
		if (model != null) {
			model.addPropertySelectionListener(propertyListener);
			setProperty(model.getSelectedProperty());		
		} else {
			setProperty(null);
		}
		
		this.model = model;
	}
	
	private void setProperty(Property property) {	
		updateLock = true;
		
		boolean validProperty = property != null;
		showPropertyLabel(!validProperty);
		showPropertyComposite(validProperty);
		if (validProperty) {
			key.setText(property.key());
			value.setText(property.value());
		}		
		
		updateLock = false;
	}

	private void showPropertyComposite(boolean show) {
		propertyComposite.setVisible(show);
		GridData gd = (GridData) propertyComposite.getLayoutData();
		gd.exclude = !show;
		layout();
	}

	private void showPropertyLabel(boolean show) {
		propertyLabel.setVisible(show);
		GridData gd = (GridData) propertyLabel.getLayoutData();
		gd.exclude = !show;
		layout();
	}
}
