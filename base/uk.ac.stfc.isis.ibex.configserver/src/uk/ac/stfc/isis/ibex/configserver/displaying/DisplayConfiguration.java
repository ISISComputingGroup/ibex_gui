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

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.Displaying;
import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.Group;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.observing.TransformingObservable;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlServer;

import com.google.common.base.Strings;

public class DisplayConfiguration extends TransformingObservable<Configuration, DisplayConfiguration> implements
		Displaying {

	private String name;
	private String description;
	private String defaultSynoptic;
	private final List<DisplayGroup> groups = new ArrayList<>();
	private Configuration config;
	private Collection<DisplayBlock> displayBlocks;

	private final ConfigServer configServer;
	private final RunControlServer runControlServer;

	public DisplayConfiguration(ClosableCachingObservable<Configuration> config, ConfigServer configServer,
			RunControlServer runControlServer) {
		this.configServer = configServer;
		this.runControlServer = runControlServer;
		setSource(config);
	}

	@Override
	protected DisplayConfiguration transform(Configuration value) {
		this.config = value;
		name = value.name();
		description = value.description();
		defaultSynoptic = value.synoptic();
		setDisplayBlocks(value.getBlocks());
		setGroups(value.getGroups());
		return this;
	}

	@Override
	public InitialiseOnSubscribeObservable<DisplayConfiguration> displayCurrentConfig() {
		return new InitialiseOnSubscribeObservable<>(this);
	}

	@Override
	public Collection<DisplayBlock> getDisplayBlocks() {
		return displayBlocks;
	}

	public String name() {
		return Strings.nullToEmpty(name);
	}

	public String description() {
		return Strings.nullToEmpty(description);
	}

	public String defaultSynoptic() {
		return Strings.nullToEmpty(defaultSynoptic);
	}
	
	public Collection<DisplayGroup> groups() {
		return new ArrayList<>(groups);
	}
	
	protected void setGroups(Collection<Group> configGroups) {
		groups.clear();
		for (Group group : configGroups) {
			groups.add(new DisplayGroup(group, displayBlocks));
		}
	}

	protected void setDisplayBlocks(Collection<Block> blocks) {
		displayBlocks = new ArrayList<>();
		for (Block blk : blocks) {
			String name = blk.getName();
			displayBlocks.add(new DisplayBlock(blk, configServer.blockValue(name), configServer.blockDescription(name),
					runControlServer.blockRunControlInRange(name), runControlServer.blockRunControlLowLimit(name),
					runControlServer.blockRunControlHighLimit(name), runControlServer.blockRunControlEnabled(name),
					configServer.blockServerAlias(name)));
		}
	}
}
