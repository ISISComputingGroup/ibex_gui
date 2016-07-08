
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

package uk.ac.stfc.isis.ibex.synoptic.internal;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import uk.ac.stfc.isis.ibex.devicescreens.desc.Component;
import uk.ac.stfc.isis.ibex.devicescreens.desc.ComponentProperty;
import uk.ac.stfc.isis.ibex.devicescreens.desc.ComponentType;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;

public abstract class BaseComponent implements Component {

	private final ComponentDescription description;
	private Set<ComponentProperty> properties = new LinkedHashSet<>();
	private List<Component> components = new ArrayList<>();
	
	public BaseComponent(ComponentDescription description) {
		this.description = description;
	}

	public BaseComponent(BaseComponent other) {
		this.description = other.description;
		this.properties = other.properties;
		this.components = other.components;
	}
	
	@Override
	public String name() {
		return description.name();
	}

	@Override
	public ComponentType type() {
		return description.type();
	}

	@Override
	public Set<ComponentProperty> properties() {
		return new LinkedHashSet<>(properties);
	}

	@Override
	public List<? extends Component> components() {
		return new ArrayList<>(components);
	}

	protected void addProperty(ComponentProperty property) {
		properties.add(property);
	}

	protected void addComponent(Component component) {
		components.add(component);
	}
	
	protected ComponentDescription description() {
		return description;
	}
}
