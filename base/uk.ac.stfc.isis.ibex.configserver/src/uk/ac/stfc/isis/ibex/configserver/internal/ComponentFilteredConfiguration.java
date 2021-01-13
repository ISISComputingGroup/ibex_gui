
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.Group;
import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;

/**
 * A configuration from which component-derived elements have been filtered.
 */
public class ComponentFilteredConfiguration extends Configuration {
	
    /**
     * Constructor for the component-filtered configuration.
     * 
     * @param other
     *            The unfiltered configuration
     */
	public ComponentFilteredConfiguration(Configuration other) {
        super(other.name(), other.description(), other.synoptic(), filterIocs(other.getIocs()),
                filterBlocks(other.getBlocks()), filterGroups(other.getGroups()), other.getComponents(),
                other.getHistory(), other.isProtected(), other.configuresBlockGWAndArchiver());
	}
	
    /**
     * Takes a collection of IOCs and filters out the ones that are part of a
     * component.
     * 
     * @param iocs
     *            The IOCs
     * @return The filtered collection of IOCs
     */
    public static Collection<Ioc> filterIocs(Collection<Ioc> iocs) {
        return iocs.stream()
        		.filter(ioc -> !ioc.inComponent())
        		.collect(Collectors.toCollection(ArrayList::new));
	}

    /**
     * Takes a collection of blocks and filters out the ones that are part of a
     * component.
     * 
     * @param blocks
     *            The blocks
     * @return The filtered collection of blocks
     */
    public static Collection<Block> filterBlocks(Collection<Block> blocks) {
        return blocks.stream()
        		.filter(block -> !block.inComponent())
        		.collect(Collectors.toCollection(ArrayList::new));
	}

    /**
     * Takes a collection of groups and returns a filtered list. Configuration
     * groups are unaltered. Component groups are cleared of blocks and labelled
     * as component groups. This assumes groups cannot be jointly members of
     * configurations and components.
     * 
     * @param groups
     *            The groups
     * @return The filtered collection of groups
     */
    public static List<Group> filterGroups(Collection<Group> groups) {
        return groups.stream()
        		.map(group -> group.getComponent() == null ? group : new Group(group.getName(), new ArrayList<String>(), group.getComponent()))
        		.collect(Collectors.toCollection(ArrayList::new));
	}
}
