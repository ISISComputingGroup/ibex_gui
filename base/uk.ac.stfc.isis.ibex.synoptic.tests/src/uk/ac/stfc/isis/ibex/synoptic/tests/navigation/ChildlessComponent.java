
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

package uk.ac.stfc.isis.ibex.synoptic.tests.navigation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import uk.ac.stfc.isis.ibex.devicescreens.desc.Component;
import uk.ac.stfc.isis.ibex.devicescreens.desc.ComponentProperty;
import uk.ac.stfc.isis.ibex.devicescreens.desc.ComponentType;
import uk.ac.stfc.isis.ibex.synoptic.model.targets.GroupedComponentTarget;
import uk.ac.stfc.isis.ibex.targets.Target;

public class ChildlessComponent implements Component {

	private String name;
	private GroupedComponentTarget target;
	
	public ChildlessComponent(String name) {
		this.name = name;
		target = new GroupedComponentTarget(name, new ArrayList<Component>());
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
		return new ArrayList<Component>();
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
