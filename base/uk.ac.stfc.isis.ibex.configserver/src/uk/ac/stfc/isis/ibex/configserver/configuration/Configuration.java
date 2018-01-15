
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

import uk.ac.stfc.isis.ibex.configserver.internal.ConfigEditing;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * This is the base class that holds all information relating to a BlockServer
 * configuration.
 * 
 * Note: The values from this class are populated from the BlockServer JSON via
 * reflection. Therefore variable names must reflect those expected from the
 * JSON.
 */
public class Configuration extends ModelObject {
	
	private String name;
	private String description;
	private String synoptic;
	private String pv;
	
	private List<Ioc> iocs = new ArrayList<>();
	private List<Block> blocks = new ArrayList<>();
	private List<Group> groups = new ArrayList<>();
	private List<ComponentInfo> components = new ArrayList<>();
	private List<String> history = new ArrayList<>();
	
    /**
     * Create a new configuration.
     * 
     * @param name
     *            The configuration name
     * @param description
     *            The configuration description
     */
	public Configuration(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
    /**
     * Create a new configuration.
     * 
     * @param name
     *            The configuration name
     * @param description
     *            The configuration description
     * @param defaultSynoptic
     *            The default synoptic to open when the configuration is current
     * @param iocs
     *            The IOCs associated with the configuration
     * @param blocks
     *            The configuration's blocks
     * @param groups
     *            The configuration's groups
     * @param components
     *            The configuration's components
     * @param history
     *            A collection of dates when the configuration was updated
     */
	public Configuration(
			String name, 
			String description,
			String defaultSynoptic,
			Collection<Ioc> iocs,
			Collection<Block> blocks, 
			Collection<Group> groups, 
			Collection<ComponentInfo> components, 
			Collection<String> history) {
		this.name = name;
		this.description = description;
		this.synoptic = defaultSynoptic.equals("") ? ConfigEditing.NONE_SYNOPTIC_NAME : defaultSynoptic;
		this.pv = "";
		
		for (Ioc ioc : iocs) {
			this.iocs.add(new Ioc(ioc));
		}
		
		for (Block block : blocks) {
			this.blocks.add(new Block(block));
		}
		
		for (Group group : groups) {
			this.groups.add(new Group(group));
		}
		
		for (ComponentInfo component : components) {
			this.components.add(new ComponentInfo(component));
		}
		
		for (String date : history) {
			this.history.add(date);
		}
	}
	
    /**
     * Create a new configuration matching the properties of another.
     * 
     * @param other
     *            The other configuration.
     */
	public Configuration(Configuration other) {
		this(other.name(), other.description(), other.synoptic(), other.getIocs(), other.getBlocks(), other.getGroups(), other.getComponents(), other.getHistory());
		this.pv = other.pv;
	}
	
    /**
     * @return The name of the configuration
     */
	public String name() {
		return name;
	}
	
    /**
     * @return The description of the configuration
     */
	public String description() {
		return description;
	}
	
    /**
     * @return The configuration's default synoptic
     */
	public String synoptic() {
		return synoptic;
	}
	
    /**
     * @return The PV used to get the configuration
     */
	public String pv() {
		return pv;
	}
	
    /**
     * @param name
     *            The new configuration name
     */
	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}
	
    /**
     * @param description
     *            The new configuration description
     */
	public void setDescripion(String description) {
		firePropertyChange("description", this.description, this.description = description);
	}
	
    /**
     * @return A collection of the IOCs associated with the configuration
     */
	public Collection<Ioc> getIocs() {	
		return new ArrayList<>(iocs);
	}

    /**
     * @return A collection of the configuration's blocks
     */
	public Collection<Block> getBlocks() {	
		return blocks != null ? new ArrayList<>(blocks) : Collections.<Block>emptyList();
	}
		
    /**
     * @return A collection of the configuration's groups
     */
	public Collection<Group> getGroups() {
		return groups != null ? new ArrayList<>(groups) : Collections.<Group>emptyList();
	}
	
    /**
     * @return A collection of the components associated with the configuration
     */
	public Collection<ComponentInfo> getComponents() {
		return new ArrayList<>(components);
	}
	
    /**
     * @return A collection of dates (as Strings) when the configuration was
     *         updated
     */
	public Collection<String> getHistory() {
		return new ArrayList<>(history);
	}
	
	@Override
	public String toString() {
		return name;
	}

    /**
     * Method needed for data binding.
     * 
     * @return The name of the configuration.
     */
    public String getName() {
        return name;
    }
}
