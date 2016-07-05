
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

package uk.ac.stfc.isis.ibex.synoptic.model.targets;

import java.util.Arrays;

import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.Property;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetDescription;
import uk.ac.stfc.isis.ibex.targets.Target;

public class TargetBuilder {
		
	private final Component component;
	private final TargetDescription description;
	
	private Target target;
	
	public TargetBuilder(Component component) {
		this(component, null);
	}
	
	public TargetBuilder(Component component, TargetDescription description) {
		this.component = component;
		this.description = description;
		
		target = getTarget(component);
	}
	
	public Target target() {
		return target;
	}
	
	private Target getTarget(Component component) {		
		Target target = componentTarget() != null ? componentTarget() : targetFromType(description);
		return isGroup() ? targetForGroup(target) : target;
	}
	
	private Target componentTarget() {
		return component.type().target(); 
	}
	
	private Target targetForGroup(Target target) {
		// A group's first target is the grouped view; the second, the actual target.
		Component copy = component.copy();
		copy.setTarget(target);
		return new GroupedComponentTarget(component.name(), Arrays.asList(copy));
	}

	private Target targetFromType(TargetDescription description) {
		if (description == null) {
			return null;
		}
		
		switch (description.type()) {
			case OPI:
				return opiTarget(description);
			default:
				return null;
		}
	}

	private Target opiTarget(TargetDescription description) {		
		String name = component.name();
		OpiTarget opiTarget = new OpiTarget(name, description.name());
		for (Property property : description.getProperties()) {
			opiTarget.addProperty(property.key(), property.value());
		}
		
		return opiTarget;
	}
	
	
	private boolean isGroup() {
		return !component.components().isEmpty();
	}
}
