
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

import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.ui.devicescreens.ComponentIcons;
import uk.ac.stfc.isis.ibex.ui.synoptic.beamline.BeamlineComposite;
import uk.ac.stfc.isis.ibex.ui.synoptic.beamline.BeamlineCompositeContainer;

/**
 * The view in the synoptic perspective of the associated components.
 */
public final class ComponentView {
	
	private ComponentView() { }

	public static BeamlineComposite create(BeamlineCompositeContainer parent, Component component) {
		BeamlineComposite target = component.components().isEmpty() 
									? createBasicComponent(parent, component) 
									: createGroupView(parent, component);
				
		parent.registerBeamlineTarget(target);
		
		return target;
	}

	private static GroupView createGroupView(Composite parent, Component component) {
		return new GroupView(parent, component);
	}

	private static BasicComponent createBasicComponent(Composite parent, Component component) {
		BasicComponent componentView = new BasicComponent(parent);
		componentView.setName(component.name());
        componentView.setImage(ComponentIcons.iconForType(component.type()));
		componentView.setProperties(component);
		if (component.target() != null) {
            componentView.setTarget(component.target());
		}
		return componentView;
	}
}
