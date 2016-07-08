
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
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.devicescreens.desc.Component;
import uk.ac.stfc.isis.ibex.ui.synoptic.Activator;
import uk.ac.stfc.isis.ibex.ui.synoptic.SynopticPresenter;
import uk.ac.stfc.isis.ibex.ui.synoptic.beamline.BeamlineComposite;
import uk.ac.stfc.isis.ibex.ui.synoptic.beamline.BeamlineCompositeContainer;

@SuppressWarnings("checkstyle:magicnumber")
public class GroupView extends BeamlineComposite {

    private final Cursor handCursor = SWTResourceManager.getCursor(SWT.CURSOR_HAND);

	private SynopticPresenter presenter = Activator.getDefault().presenter();
	private String targetName;

	private Composite groupPropertiesComposite;
	private CLabel groupName;
	private BeamlineCompositeContainer groupComponents;
	private Component component;
	
	/**
	 * @wbp.parser.constructor
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
		
		groupName = new CLabel(this, SWT.BORDER | SWT.CENTER);
		groupName.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		groupName.setFont(SWTResourceManager.getFont("Arial", 12, SWT.BOLD));
		GridData gdGroupName = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gdGroupName.heightHint = 30;
		gdGroupName.minimumHeight = 30;
		groupName.setLayoutData(gdGroupName);
		groupName.setAlignment(SWT.CENTER);
		groupName.setBackground(SWTResourceManager.getColor(111, 94, 230));
		
		groupName.addListener(SWT.MouseEnter, new Listener() {	
			@Override
			public void handleEvent(Event event) {
				setCursor(targetName == null ? null : handCursor);
			}
		});

		groupName.addListener(SWT.MouseExit, new Listener() {	
			@Override
			public void handleEvent(Event event) {
				setCursor(null);
			}
		});
				
		groupName.addListener(SWT.MouseUp, new Listener() {		
			@Override
			public void handleEvent(Event event) {
				presenter.navigateTo(targetName);
			}
		});
		
		
		groupComponents = new BeamlineCompositeContainer(this, SWT.NONE);
		
		if (component != null) {
			setName(component);
			setProperties(component);
			setComponents(component);
			if (component.target() != null) {
				setTargetName(component.target().name());
			}
		}
	}

	private void setTargetName(String name) {
		targetName = component.target().name();
		groupName.setToolTipText(targetName);
	}

	@Override
	public int beamLineHeight() {
        return component.components().isEmpty() ? nameControlCentreLine()
                : componentsTargetLineHeight() + nameControlBottomLine();
	}
	
	private void setName(Component component) {
		groupName.setText(component.name());
	}

	private int componentsTargetLineHeight() {       
		return groupComponents.getBounds().y + groupComponents.beamLineHeight();
	}

	private int nameControlCentreLine() {
		return groupName.getBounds().y + groupName.getBounds().height / 2;
	}
	
    private int nameControlBottomLine() {
        return groupName.getBounds().y + groupName.getBounds().height;
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
