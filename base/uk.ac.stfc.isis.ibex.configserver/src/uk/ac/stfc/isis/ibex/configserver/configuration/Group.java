
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

package uk.ac.stfc.isis.ibex.configserver.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Represents a group of blocks within a configuration.
 * 
 * Note: The values from this class are populated from the BlockServer JSON via
 * reflection. Therefore variable names must reflect those expected from the
 * JSON.
 */
public class Group extends ModelObject {
	
	private String name;
	private List<String> blocks = new ArrayList<>();
	private String component;
	
	/**
	 * The constructor for a group of blocks within a configuration.
	 * 
	 * @param name
	 *             The group name.
	 */
	public Group(String name) {
		this(name, Collections.<String>emptyList(), null);
	}
	
	/**
     * The constructor for a group of blocks within a configuration.
     * 
     * @param name
     *             The group name.
     * @param blocks
     *             The blocks within the group.
     * @param component
     *             The component associated with the group.
     */
	public Group(String name, Collection<String> blocks, String component) {
		this.name = name;
		for (String block : blocks) {
			this.blocks.add(block);
		}
		this.component = component;
	}
	
	/**
     * A copy constructor to build a group from another group.
     * 
     * @param other
     *             The other group.
     */
	public Group(Group other) {
		this(other.getName(), other.getBlocks(), other.component);
	}

	/**
	 * Returns the group's name.
	 * 
	 * @return
	 *         The group's name.
	 */
	public String getName() {
		return name;
	}
	
	/**
     * Sets the group's name.
     * 
     * @param name
     *         The group's name.
     */
	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}
	
	/**
     * Returns the group's blocks.
     * 
     * @return
     *         The group's blocks.
     */
	public Collection<String> getBlocks() {
		return blocks;
	}
	
	/**
     * Returns the component associated with the group.
     * 
     * @return
     *         The component associated with the group
     */
	public String getComponent() {
		return component;
	}
	
	/**
	 * Returns true if the group has a component.
	 * 
	 * @return
	 *         True if the group has a component.
	 */
	public boolean hasComponent() {
		return !Strings.isNullOrEmpty(component);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Group)) {
			return false;
		}
		
		if (obj == this) {
			return true;
		}
		
		Group other = (Group) obj;
		return name.equals(other.name) 
				&& blocks.equals(other.blocks) 
				&& (component == null ? other.component == null : component.equals(other.component)); 
	}
	
	@Override
	public int hashCode() {
		return name.hashCode() 
				^ blocks.hashCode() 
				^ (component == null ? 0 : component.hashCode());
	}
	
	@Override
	public String toString() {
		return name;
	}
}
