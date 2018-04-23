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

package uk.ac.stfc.isis.ibex.configserver.displaying;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.Displaying;
import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.Group;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.TransformingObservable;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlServer;

public class DisplayConfiguration extends TransformingObservable<Configuration, DisplayConfiguration> implements
		Displaying {

	private String name;
	private String description;
	private String defaultSynoptic;
	private final List<DisplayGroup> groups = new ArrayList<>();
	private Collection<DisplayBlock> displayBlocks;

	private final ConfigServer configServer;
	private final RunControlServer runControlServer;

	public DisplayConfiguration(ClosableObservable<Configuration> config, ConfigServer configServer,
			RunControlServer runControlServer) {
		this.configServer = configServer;
		this.runControlServer = runControlServer;
		setSource(config);
	}

	@Override
	protected DisplayConfiguration transform(Configuration value) {
		name = value.name();
		description = value.description();
		defaultSynoptic = value.synoptic();
		setDisplayBlocks(value.getBlocks());
		setGroups(value.getGroups());
		return this;
	}

	@Override
	public ForwardingObservable<DisplayConfiguration> displayCurrentConfig() {
		return new ForwardingObservable<>(this);
	}

	@Override
	public Collection<DisplayBlock> getDisplayBlocks() {
		return displayBlocks;
	}

	/**
	 * Returns the name of the configuration.
	 * 
	 * @return the name
	 */
	public String name() {
		return Strings.nullToEmpty(name);
	}

	/**
	 * Returns the description of the configuration.
	 * 
	 * @return the description
	 */
	public String description() {
		return Strings.nullToEmpty(description);
	}

	/**
	 * Returns the name of the default synoptic.
	 * 
	 * @return the default synoptic
	 */
	public String defaultSynoptic() {
		return Strings.nullToEmpty(defaultSynoptic);
	}
	
	/**
	 * Returns the groups.
	 * 
	 * @return a copy of the group
	 */
	public Collection<DisplayGroup> groups() {
		return new ArrayList<>(groups);
	}
	
	/**
	 * Sets the groups.
	 * 
	 * @param configGroups the groups based on the configuration
	 */
	protected void setGroups(Collection<Group> configGroups) {
		groups.clear();
		for (Group group : configGroups) {
			if (!isGroupEmpty(group)) {
				groups.add(new DisplayGroup(group, displayBlocks));
			}
		}
	}
	
	private boolean isGroupEmpty(Group configGroup) {
		Collection<String> blocks = configGroup.getBlocks();
        return blocks.isEmpty();
	}

	/**
	 * Sets the blocks.
	 * 
	 * @param blocks the blocks based on the configuration
	 */
	protected void setDisplayBlocks(Collection<Block> blocks) {
		displayBlocks = new ArrayList<>();
		for (Block blk : blocks) {
			String name = blk.getName();
            displayBlocks.add(new DisplayBlock(blk, configServer.blockValue(name), configServer.blockDescription(name),
                    configServer.alarm(name),
					runControlServer.blockRunControlInRange(name), runControlServer.blockRunControlLowLimit(name),
					runControlServer.blockRunControlHighLimit(name), runControlServer.blockRunControlEnabled(name),
					configServer.blockServerAlias(name)));
		}
	}
}
