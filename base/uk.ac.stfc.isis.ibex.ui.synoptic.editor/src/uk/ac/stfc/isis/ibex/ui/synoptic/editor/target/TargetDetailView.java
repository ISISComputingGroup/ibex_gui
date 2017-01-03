
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.target;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IInstrumentUpdateListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.UpdateTypes;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.target.properties.TargetPropertiesView;

/**
 * Shows the synoptic editor part that allows selection of a target, normally an
 * OPI or a Java SWT screen for goniometers or DAEs. OPI can be chosen, but
 * goniometers and DAEs have a fixed target.
 *
 */
@SuppressWarnings("checkstyle:magicnumber")
public class TargetDetailView extends Composite {

	private SynopticViewModel synopticViewModel;
    private Collection<String> availableOPIs;
	
	private Composite labelComposite;
	private Composite fieldsComposite;
    private Label lblNoSelection;

	TargetNameWidget nameSelect;

    public TargetDetailView(Composite parent, final SynopticViewModel synopticViewModel, Collection<String> availableOPIs) {
		super(parent, SWT.NONE);
		
		this.synopticViewModel = synopticViewModel;
		
		synopticViewModel.addInstrumentUpdateListener(new IInstrumentUpdateListener() {
			@Override
			public void instrumentUpdated(UpdateTypes updateType) {
				showTarget(synopticViewModel.getSingleSelectedComp());
			}
		});
        synopticViewModel.addPropertyChangeListener("compSelection", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                showTarget(synopticViewModel.getSingleSelectedComp());
            }
		});
		
        this.availableOPIs = availableOPIs;

		setLayout(new GridLayout(1, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createControls(this);
		showTarget(null);
	}
	
	public void createControls(Composite parent) {	
		labelComposite = new Composite(parent, SWT.NONE);
		labelComposite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1));
		labelComposite.setLayout(new GridLayout(1, false));

        lblNoSelection = new Label(labelComposite, SWT.NONE);
        lblNoSelection.setText("Select a Component to view/edit details");
		
		fieldsComposite = new Composite(parent, SWT.NONE);
		fieldsComposite.setLayout(new GridLayout(2, false));
		fieldsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
        Label lblName = new Label(fieldsComposite, SWT.NONE);
        lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        lblName.setText("Name");

        nameSelect = new TargetNameWidget(fieldsComposite, synopticViewModel, availableOPIs);
        nameSelect.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblDescription = new Label(fieldsComposite, SWT.NONE);
        lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        lblDescription.setText("Description");

        TargetDescriptionWidget desc = new TargetDescriptionWidget(fieldsComposite, synopticViewModel);
        GridData gdDescription = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gdDescription.heightHint = 70;
        desc.setLayoutData(gdDescription);

        TargetPropertiesView propertiesView = new TargetPropertiesView(fieldsComposite, synopticViewModel);
        propertiesView.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
	}
	
    private void showTarget(ComponentDescription component) {
		if (component == null) {
			fieldsComposite.setVisible(false);
			labelComposite.setVisible(true);
            addingAllowed(true, "");
			
			nameSelect.setTarget(null);
		} else {
			TargetDescription target = component.target();
			
            if (component.type().target() != null) {
                // a target already exits, so should not be allowed to get set
                fieldsComposite.setVisible(false);
                labelComposite.setVisible(true);
                addingAllowed(false, synopticViewModel.getSingleSelectedComp().type().name());
                nameSelect.setTarget(null);
                synopticViewModel.setSelectedProperty(null);
            } else if (target != null) {
				fieldsComposite.setVisible(true);
				labelComposite.setVisible(false);
				
				nameSelect.setTarget(target);
			} else {
				fieldsComposite.setVisible(false);
				labelComposite.setVisible(false);
				
				nameSelect.setTarget(null);
			}
		}
	}

    private void addingAllowed(boolean allowed, String component) {
        if (allowed) {
            lblNoSelection.setText("Select a Component to view/edit details");
        } else {
            lblNoSelection.setText("Target not selectable for " + component);
        }
    }
}
