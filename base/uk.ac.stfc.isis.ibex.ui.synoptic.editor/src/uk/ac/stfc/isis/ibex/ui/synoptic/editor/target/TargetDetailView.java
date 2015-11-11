
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IComponentSelectionListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IInstrumentUpdateListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.UpdateTypes;

public class TargetDetailView extends Composite {

	private SynopticViewModel instrument;
	
	private Composite labelComposite;
	private Composite fieldsComposite;
	private Composite addComposite;
    private Composite noAddComposite;

	TargetNameWidget nameSelect;

	public TargetDetailView(Composite parent, final SynopticViewModel instrument) 
	{
		super(parent, SWT.NONE);
		
		this.instrument = instrument;
		
		instrument.addInstrumentUpdateListener(new IInstrumentUpdateListener() {
			@Override
			public void instrumentUpdated(UpdateTypes updateType) {
                showTarget(instrument.getSelectedComponent());
			}
		});
		
		instrument.addComponentSelectionListener(new IComponentSelectionListener() {			
			@Override
			public void selectionChanged(ComponentDescription oldSelection, ComponentDescription newSelection) {
				showTarget(newSelection);
			}
		});
		
		setLayout(new GridLayout(1, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createControls(this);
		showTarget(null);
	}
	
	public void createControls(Composite parent) {	
		labelComposite = new Composite(parent, SWT.NONE);
		labelComposite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1));
		labelComposite.setLayout(new GridLayout(1, false));
		{
			Label lblNoSelection = new Label(labelComposite, SWT.NONE);
			lblNoSelection.setText("Select a Component to view/edit details");
		}
		
		fieldsComposite = new Composite(parent, SWT.NONE);
		fieldsComposite.setLayout(new GridLayout(2, false));
		fieldsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		{
			Label lblName = new Label(fieldsComposite, SWT.NONE);
			lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblName.setText("Name");
			
			nameSelect = new TargetNameWidget(fieldsComposite, instrument);
			nameSelect.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			Label lblDescription = new Label(fieldsComposite, SWT.NONE);
			lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
			lblDescription.setText("Description");
			
			TargetDescriptionWidget desc = new TargetDescriptionWidget(fieldsComposite, instrument);
			GridData gdDescription = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
			gdDescription.heightHint = 100;
			desc.setLayoutData(gdDescription);
			
			Label lblProperties = new Label(fieldsComposite, SWT.NONE);
			lblProperties.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
			lblProperties.setText("Properties");
			
			TargetPropertyList properties = new TargetPropertyList(fieldsComposite, instrument);
			properties.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			properties.showPropertyList(instrument.getSelectedComponent());
		}
		
		addComposite = new Composite(parent, SWT.NONE);
		addComposite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1));
		addComposite.setLayout(new GridLayout(1, false));
		{
			Label lblNoTarget = new Label(addComposite, SWT.NONE);
			lblNoTarget.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
			lblNoTarget.setText("Component currently has no target.");
			
			Button btnAdd = new Button(addComposite, SWT.NONE);
		    btnAdd.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1));
		    btnAdd.setText("Add Target");
			btnAdd.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					instrument.addTargetToSelectedComponent();
				}
			});
		}

        noAddComposite = new Composite(parent, SWT.NONE);
        noAddComposite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1));
        noAddComposite.setLayout(new GridLayout(1, false));
        {
            Label lblNoTarget = new Label(noAddComposite, SWT.NONE);
            lblNoTarget.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
            lblNoTarget.setText("OPI selection not needed for DAE or Goniometer.");
        }
	}
	
    private void showTarget(ComponentDescription component) {
		if (component == null) {
			fieldsComposite.setVisible(false);
			labelComposite.setVisible(true);
			addComposite.setVisible(false);
			
			nameSelect.setTarget(null);
		} else {
			TargetDescription target = component.target();
			
            if (component.type().target() != null) {
                // a target already exits, so should not be allowed to get set
                fieldsComposite.setVisible(false);
                labelComposite.setVisible(false);
                addComposite.setVisible(false);
                noAddComposite.setVisible(true);

                nameSelect.setTarget(null);
                instrument.setSelectedProperty(null);
            } else if (target != null) {
				fieldsComposite.setVisible(true);
				labelComposite.setVisible(false);
				addComposite.setVisible(false);
                noAddComposite.setVisible(false);
				
				nameSelect.setTarget(target);
			} else {
				fieldsComposite.setVisible(false);
				labelComposite.setVisible(false);
				addComposite.setVisible(true);
                noAddComposite.setVisible(false);
				
				nameSelect.setTarget(null);
			}
		}
	}
}
