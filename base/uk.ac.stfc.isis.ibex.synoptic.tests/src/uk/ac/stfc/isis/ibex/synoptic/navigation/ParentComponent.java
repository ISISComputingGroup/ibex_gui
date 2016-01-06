
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

package uk.ac.stfc.isis.ibex.synoptic.navigation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.synoptic.model.ComponentProperty;
import uk.ac.stfc.isis.ibex.synoptic.model.ComponentType;
import uk.ac.stfc.isis.ibex.synoptic.model.Target;
import uk.ac.stfc.isis.ibex.synoptic.model.targets.GroupedComponentTarget;

public class ParentComponent implements Component {

	private final String name;
	private final List<Component> components = new ArrayList<>();
	private final GroupedComponentTarget target;
	
	public ParentComponent(String name, Component...components) {
		this.name = name;
		this.components.addAll(Arrays.asList(components));
		target = new GroupedComponentTarget(name, this.components);
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public ComponentType type() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ComponentProperty> properties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends Component> components() {
		return components;
	}

	@Override
	public Target target() {
		return target;
	}

	@Override
	public void setTarget(Target target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component copy() {
		// TODO Auto-generated method stub
		return null;
	}
}
