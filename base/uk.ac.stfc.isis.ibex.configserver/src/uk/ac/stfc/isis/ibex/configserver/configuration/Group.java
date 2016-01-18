
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

import com.google.common.base.Strings;

public class Group extends ModelObject {
	
	private String name;
	private List<String> blocks = new ArrayList<>();
	private String subconfig;
	
	public Group(String name) {
		this(name, Collections.<String>emptyList(), null);
	}
	
	public Group(String name, Collection<String> blocks, String subconfig) {
		this.name = name;
		for (String block : blocks) {
			this.blocks.add(block);
		}
		this.subconfig = subconfig;
	}
	
	public Group(Group other) {
		this(other.getName(), other.getBlocks(), other.subconfig);
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}
	
	public Collection<String> getBlocks() {
		return blocks;
	}
	
	public String subconfig() {
		return subconfig;
	}
	
	public boolean hasSubConfig() {
		return !Strings.isNullOrEmpty(subconfig);
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
				&& (subconfig == null ? other.subconfig == null : subconfig.equals(other.subconfig)); 
	}
	
	@Override
	public int hashCode() {
		return name.hashCode() 
				^ blocks.hashCode() 
				^ (subconfig == null ? 0 : subconfig.hashCode());
	}
	
	@Override
	public String toString() {
		return name;
	}
}
