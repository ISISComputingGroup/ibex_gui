
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

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class Configuration extends ModelObject {
	
	private String name;
	private String description;
	private String synoptic;
	private String pv;
	
	private List<Ioc> iocs = new ArrayList<>();
	private List<Block> blocks = new ArrayList<>();
	private List<Group> groups = new ArrayList<>();
	private List<Component> components = new ArrayList<>();
	private List<String> history = new ArrayList<>();
	
	public Configuration(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public Configuration(
			String name, 
			String description,
			String defaultSynoptic,
			Collection<Ioc> iocs,
			Collection<Block> blocks, 
			Collection<Group> groups, 
			Collection<Component> components, 
			Collection<String> history) {
		this.name = name;
		this.description = description;
		this.synoptic = defaultSynoptic;
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
		
		for (Component component : components) {
			this.components.add(new Component(component));
		}
		
		for (String date : history) {
			this.history.add(date);
		}
	}
	
	public Configuration(Configuration other) {
		this(other.name(), other.description(), other.synoptic(), other.getIocs(), other.getBlocks(), other.getGroups(), other.getComponents(), other.getHistory());
		this.pv = other.pv;
	}
	
	public String name() {
		return name;
	}
	
	public String description() {
		return description;
	}
	
	public String synoptic() {
		return synoptic;
	}
	
	public String pv() {
		return pv;
	}
	
	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}
	
	public void setDescripion(String description) {
		firePropertyChange("description", this.description, this.description = description);
	}
	
	public Collection<Ioc> getIocs() {	
		return new ArrayList<>(iocs);
	}

	public Collection<Block> getBlocks() {	
		return blocks != null ? new ArrayList<>(blocks) : Collections.<Block>emptyList();
	}
		
	public Collection<Group> getGroups() {
		return groups != null ? new ArrayList<>(groups) : Collections.<Group>emptyList();
	}
	
	public Collection<Component> getComponents() {
		return new ArrayList<>(components);
	}
	
	public Collection<String> getHistory() {
		return new ArrayList<>(history);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
