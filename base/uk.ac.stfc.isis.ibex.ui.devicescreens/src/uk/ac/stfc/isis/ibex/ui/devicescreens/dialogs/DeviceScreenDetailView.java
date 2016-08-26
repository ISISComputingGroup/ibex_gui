
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

package uk.ac.stfc.isis.ibex.ui.devicescreens.dialogs;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;

/**
 * The editor panel for individual device screens.
 *
 */
@SuppressWarnings("checkstyle:magicnumber")
public class DeviceScreenDetailView extends Composite {

    // private SynopticViewModel synopticViewModel;
    private Collection<String> availableOPIs;
	private Composite detailssComposite;

    private TargetNameWidget targetSelect;

    public DeviceScreenDetailView(Composite parent, Collection<String> availableOPIs) {
        super(parent, SWT.NONE);
		
//		this.synopticViewModel = synopticViewModel;
//		
//		synopticViewModel.addInstrumentUpdateListener(new IInstrumentUpdateListener() {
//			@Override
//			public void instrumentUpdated(UpdateTypes updateType) {
//				showTarget(synopticViewModel.getFirstSelectedComponent());
//			}
//		});
//		
//		synopticViewModel.addComponentSelectionListener(new IComponentSelectionListener() {			
//			@Override
//			public void selectionChanged(List<ComponentDescription> oldSelection, List<ComponentDescription> newSelection) {
//				if (newSelection != null && newSelection.size() == 1) {
//					showTarget(newSelection.iterator().next());
//				} else {
//					showTarget(null);
//				}
//			}
//		});
		
        this.availableOPIs = availableOPIs;

		setLayout(new GridLayout(1, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createControls(this);
		showTarget(null);
	}
	
    public void createControls(Composite parent) {

        Group grpDetails = new Group(this, SWT.NONE);
        grpDetails.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        grpDetails.setText("Device Screens");
        grpDetails.setLayout(new GridLayout(1, false));
		
        detailssComposite = new Composite(grpDetails, SWT.NONE);
        detailssComposite.setLayout(new GridLayout(2, false));
		detailssComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
        Label lblName = new Label(detailssComposite, SWT.NONE);
        lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        lblName.setText("Name");

        Text txtName = new Text(detailssComposite, SWT.BORDER);
        txtName.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

        Label lblTarget = new Label(detailssComposite, SWT.NONE);
        lblTarget.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        lblTarget.setText("Target");

        targetSelect = new TargetNameWidget(detailssComposite, availableOPIs);
        targetSelect.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblDescription = new Label(detailssComposite, SWT.NONE);
        lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        lblDescription.setText("Description");

        TargetDescriptionWidget desc = new TargetDescriptionWidget(detailssComposite);
        GridData gdDescription = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gdDescription.heightHint = 70;
        desc.setLayoutData(gdDescription);

        TargetPropertiesView propertiesView = new TargetPropertiesView(detailssComposite);
        propertiesView.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
	}
	
    private void showTarget(ComponentDescription component) {
//		if (component == null) {
//			fieldsComposite.setVisible(false);
//			labelComposite.setVisible(true);
//            addingAllowed(true, "");
//			
//			nameSelect.setTarget(null);
//		} else {
//			TargetDescription target = component.target();
//			
//            if (component.type().target() != null) {
//                // a target already exits, so should not be allowed to get set
//                fieldsComposite.setVisible(false);
//                labelComposite.setVisible(true);
//                addingAllowed(false, synopticViewModel.getFirstSelectedComponent().type().name());
//                nameSelect.setTarget(null);
//                synopticViewModel.setSelectedProperty(null);
//            } else if (target != null) {
//				fieldsComposite.setVisible(true);
//				labelComposite.setVisible(false);
//				
//				nameSelect.setTarget(target);
//			} else {
//				fieldsComposite.setVisible(false);
//				labelComposite.setVisible(false);
//				
//				nameSelect.setTarget(null);
//			}
//		}
	}

    private void addingAllowed(boolean allowed, String component) {
//        if (allowed) {
//            lblNoSelection.setText("Select a Component to view/edit details");
//        } else {
//            lblNoSelection.setText("Target not selectable for " + component);
//        }
    }
}
