
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

package uk.ac.stfc.isis.ibex.configserver.internal;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.Group;
import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/*
 * A configuration from which component-derived 
 * elements have been filtered.
 */
public class ComponentFilteredConfiguration extends Configuration {
	
	public ComponentFilteredConfiguration(Configuration other) {
		super(other);
	}
	
	@Override
	public Collection<Ioc> getIocs() {
		return Lists.newArrayList(Iterables.filter(super.getIocs(), new Predicate<Ioc>() {
			@Override
			public boolean apply(Ioc ioc) {
				return !ioc.hasComponent();
			}
		}));
	}

	@Override
	public Collection<Block> getBlocks() {
		return Lists.newArrayList(Iterables.filter(super.getBlocks(), new Predicate<Block>() {
			@Override
			public boolean apply(Block block) {
				return !block.hasComponent();
			}
		}));
	}
	
	@Override
	public Collection<Group> getGroups() {
		return Lists.newArrayList(Iterables.filter(super.getGroups(), new Predicate<Group>() {
			@Override
			public boolean apply(Group group) {
				return !group.hasComponent();
			}
		}));
	}
}
