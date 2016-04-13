
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
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.ui.synoptic.Activator;
import uk.ac.stfc.isis.ibex.ui.synoptic.SynopticPresenter;
import uk.ac.stfc.isis.ibex.ui.synoptic.beamline.BeamlineComposite;

@SuppressWarnings("checkstyle:magicnumber")
public class BasicComponent extends BeamlineComposite {

	private Label image;
	private Label componentName;
	private Composite propertiesComposite;
	
	private SynopticPresenter presenter = Activator.getDefault().presenter();
	
	private String targetName;
	
	private final Cursor handCursor = new Cursor(getDisplay(), SWT.CURSOR_HAND);
	
	public BasicComponent(Composite parent) {
		super(parent, SWT.NONE);
		
		GridLayout gd = new GridLayout(1, false);
		gd.marginHeight = 0;
		gd.marginWidth = 0;
		gd.marginLeft = 0;
		gd.marginRight = 0;
		setLayout(gd);
		
		image = new Label(this, SWT.NONE);
		image.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1));
		image.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.synoptic", "icons/cog.png"));
		
		image.addListener(SWT.MouseEnter, new Listener() {	
			@Override
			public void handleEvent(Event event) {
                if (presenter.hasTarget(targetName)) {
			        setCursor(targetName == null ? null : handCursor);
			    }
			}
		});

		image.addListener(SWT.MouseExit, new Listener() {	
			@Override
			public void handleEvent(Event event) {
				setCursor(null);
			}
		});
				
		image.addListener(SWT.MouseUp, new Listener() {		
			@Override
			public void handleEvent(Event event) {
				if (targetName != null) {
                    if (presenter.hasTarget(targetName)) {
                        presenter.navigateTo(targetName);
                    }
				}
			}
		});
		
		componentName = new Label(this, SWT.NONE);
		componentName.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));
		componentName.setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
		componentName.setText("Component name");
		
		propertiesComposite = new Composite(this, SWT.NONE);
		propertiesComposite.setLayout(new GridLayout(1, false));
		propertiesComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}
	
	public void setName(String name) {
		componentName.setText(name);
	}
	
	public void setImage(Image icon) {
		image.setImage(icon);
	}
	
	public void setTargetName(String targetName) {
		this.targetName = targetName;
		image.setToolTipText(targetName);
	}
	
	@Override
	public int beamLineHeight() {
        final Rectangle imageBounds = image.getBounds();
		return imageBounds.y + imageBounds.height / 2;
	}
		
	public void setProperties(Component component) {
		if (component.properties().isEmpty()) {
			return;
		}
		
		ComponentPropertiesView propertiesView = new ComponentPropertiesView(propertiesComposite, component);
		
		propertiesView.pack();
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd.minimumHeight = propertiesView.getSize().y;
		gd.heightHint = gd.minimumHeight; 
		propertiesView.setLayoutData(gd);
	}
}
