
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.ui.synoptic.beamline.BeamlineComposite;
import uk.ac.stfc.isis.ibex.ui.synoptic.beamline.BeamlineCompositeContainer;

@SuppressWarnings("checkstyle:magicnumber")
public class GroupView extends BeamlineComposite {
	private Composite groupPropertiesComposite;
	private BeamlineCompositeContainer groupComponents;
	private Component component;
	
	/**
     * @param parent
     *            The parent component
     */
	public GroupView(Composite parent) {
		this(parent, null);
	}
	
	public GroupView(Composite parent, Component component) {
		super(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		this.component = component;
		
		groupPropertiesComposite = new Composite(this, SWT.NONE);
		groupPropertiesComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		groupPropertiesComposite.setLayout(new GridLayout(1, false));
		
        nameLabel = new CLabel(this, SWT.BORDER | SWT.CENTER);
        nameLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        nameLabel.setFont(SWTResourceManager.getFont("Arial", 12, SWT.BOLD));
		GridData gdGroupName = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gdGroupName.heightHint = 30;
		gdGroupName.minimumHeight = 30;
        nameLabel.setLayoutData(gdGroupName);
        nameLabel.setAlignment(SWT.CENTER);
        nameLabel.setBackground(SWTResourceManager.getColor(111, 94, 230));
		
		groupComponents = new BeamlineCompositeContainer(this, SWT.NONE);
		
		if (component != null) {
            setName(component.name());
			setProperties(component);
			setComponents(component);
			if (component.target() != null) {
                setTarget(component.target());
			}
		}
	}

	@Override
	public int beamLineHeight() {
        return component.components().isEmpty() ? nameControlCentreLine()
                : componentsTargetLineHeight() + nameControlBottomLine();
	}

	private int componentsTargetLineHeight() {       
		return groupComponents.getBounds().y + groupComponents.beamLineHeight();
	}

	private int nameControlCentreLine() {
        return nameLabel.getBounds().y + nameLabel.getBounds().height / 2;
	}
	
    private int nameControlBottomLine() {
        return nameLabel.getBounds().y + nameLabel.getBounds().height;
    }

	private void setProperties(Component component) {		
		if (component.properties().isEmpty()) {
			hideProperties();
		} else {
			addPropertiesView(component);
		}
	}

	private void hideProperties() {
		groupPropertiesComposite.setVisible(false);
	}

	private void addPropertiesView(Component component) {
		ComponentPropertiesView propertiesView = new ComponentPropertiesView(groupPropertiesComposite, component);
		
		propertiesView.pack();
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd.minimumHeight = propertiesView.getSize().y;
		gd.heightHint = gd.minimumHeight; 
		propertiesView.setLayoutData(gd);
	}

	private void setComponents(Component component) {
		for (Component child : component.components()) {
			BeamlineComposite view = ComponentView.create(groupComponents, child);
			groupComponents.registerBeamlineTarget(view);
		}
	}

}
