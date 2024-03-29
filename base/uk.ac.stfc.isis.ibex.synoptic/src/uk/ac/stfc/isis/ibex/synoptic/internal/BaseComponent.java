
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

import uk.ac.stfc.isis.ibex.devicescreens.components.ComponentType;
import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.synoptic.model.ComponentProperty;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;

/**
 * An abstract base class for components in the synoptic.
 */
public abstract class BaseComponent implements Component {

	private final ComponentDescription description;
	private Set<ComponentProperty> properties = new LinkedHashSet<>();
	private List<Component> components = new ArrayList<>();
	
	/**
	 * Constructor for a component from a component description.
	 * 
	 * @param description the ComponentDescription
	 */
	public BaseComponent(ComponentDescription description) {
		this.description = description;
	}

	/**
	 * Constructor to create a copy of a component.
	 * 
	 * @param other the other component to copy
	 */
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

	/**
	 * Add the given property to the base component's properties.
	 * 
	 * @param property The property to add.
	 */
	protected void addProperty(ComponentProperty property) {
		properties.add(property);
	}

	/**
	 * Add a component to the base component's components.
	 * 
	 * @param component The component to add.
	 */
	protected void addComponent(Component component) {
		components.add(component);
	}
	
	/**
	 * Get the description of this component.
	 * 
	 * @return The component's description.
	 */
	protected ComponentDescription description() {
		return description;
	}
}
