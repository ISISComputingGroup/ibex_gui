/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2019 Science & Technology Facilities Council.
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.alerts.AlertsServer;
import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.Displaying;
import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.Group;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.TransformingObservable;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlServer;

/**
 * A class to enable displaying configurations to a GUI.
 *
 */
public class DisplayConfiguration extends TransformingObservable<Configuration, DisplayConfiguration> implements
		Displaying {

	private String name;
	private String description;
	private String defaultSynoptic;
	private boolean isProtected;
	private boolean isDynamic;
	private boolean configuresBlockGWAndArchiver;
	private final List<DisplayGroup> groups = new ArrayList<>();
	private Collection<DisplayBlock> displayBlocks;

	private final ConfigServer configServer;
	private final RunControlServer runControlServer;
	private final AlertsServer alertsServer;
	private Collection<DisplayAlerts> displayAlerts;
	private TopLevelAlertSettings topLevelAlertSettings;

	/**
	 * The constructor for a class to enable displaying configurations to a GUI.
	 *
	 * @param config
	 *                 The config to be displayed.
	 * @param configServer
	 *                 The config server to which the config belongs.
	 * @param runControlServer
	 *                 The run control server controlling the state of the config.
	 * @param alertsServer The alerts server providing alerts for the blocks in the config.	
	 */
	public DisplayConfiguration(ClosableObservable<Configuration> config, ConfigServer configServer,
			RunControlServer runControlServer, AlertsServer alertsServer) {
		this.configServer = configServer;
		this.runControlServer = runControlServer;
		this.alertsServer = alertsServer;
		setSource(config);
	}

	@Override
	protected DisplayConfiguration transform(Configuration value) {
		name = value.name();
		description = value.description();
		defaultSynoptic = value.synoptic();
		isProtected = value.isProtected();
		isDynamic = value.isDynamic();
		configuresBlockGWAndArchiver = value.configuresBlockGWAndArchiver();
		setDisplayBlocks(value.getBlocks());
		setGroups(value.getGroups());
		setDisplayAlerts(value.getBlocks());
		setTopLevelAlertSettings();
		return this;
	}

	@Override
	public ForwardingObservable<DisplayConfiguration> displayCurrentConfig() {
		return new ForwardingObservable<>(this);
	}

	@Override
	public Collection<DisplayBlock> getDisplayBlocks() {
		return new ArrayList<>(displayBlocks);
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
	*Returns if the protected flag is set or not.
	*@return the protected flag
	*
	*/
	public boolean isProtected() {
		return isProtected;
	}
	
	/**
	*Returns if the dynamic flag is set or not.
	*@return the dynamic flag
	*
	*/
	public boolean isDynamic() {
		return isDynamic;
	}

	/**
	 * @return Whether the configuration configures the block gateway and archiver.
	 */
	public boolean configuresBlockGWAndArchiver() {
		return configuresBlockGWAndArchiver;
	}

	/**
	 * Returns the groups.
	 *
	 * @return a copy of the group
	 */
	public List<DisplayGroup> groups() {
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
		return configGroup.getBlocks().isEmpty();
	}

	/**
	 * Sets the blocks.
	 *
	 * @param blocks the blocks based on the configuration
	 */
	protected void setDisplayBlocks(Collection<Block> blocks) {

		// Close old display blocks.
		if (displayBlocks != null) {
			displayBlocks.forEach(DisplayBlock::close);
		}

		displayBlocks = new ArrayList<>();
		for (Block blk : blocks) {
			String name = blk.getName();
            displayBlocks.add(
        		new DisplayBlock(
    				blk,
            		configServer.blockValue(name),                    
            		configServer.blockDescription(name),
                    configServer.alarm(name),
					runControlServer.blockRunControlInRange(name),
					runControlServer.blockRunControlLowLimit(name),
					runControlServer.blockRunControlHighLimit(name),
					runControlServer.blockRunControlSuspendIfInvalid(name),
					runControlServer.blockRunControlEnabled(name),
					configServer.blockServerAlias(name)
				)
        	);
		}
	}
	
    /**
     * Creates a warning informing the user of a conflict.
     * @param blockConflicts a map containing the block conflicts
     * @param iocConflicts a map containing the IOC conflicts
     * @param action what the user is trying to do
     * @param element the thing that the user is trying to do something to
     * @return the warning string
     */
    public static String buildWarning(Map<String, Set<String>> blockConflicts, Map<String, Set<String>> iocConflicts,
            String action, String element) {
        Map<String, Map<String, Set<String>>> conflicts = new HashMap<>();
        if (iocConflicts.size() > 0) {
            conflicts.put("IOC", iocConflicts);
        }
        if (blockConflicts.size() > 0) {
            conflicts.put("Block", blockConflicts);
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Cannot %s the %s as it would result in the following duplicate elements:\n\n", action, element));

        for (String conflictType : conflicts.keySet()) {
            for (String item : conflicts.get(conflictType).keySet()) {
                sb.append(conflictType.substring(0, 1).toUpperCase() + conflictType.substring(1) + " \"" + item + "\" contained in:\n");
                Set<String> sources = conflicts.get(conflictType).get(item);
                for (String source : sources) {
                    sb.append("\u2022 " + source + "\n");
                }
                sb.append("\n");
            }
        }
        
        sb.append("Please resolve these conflicts by removing / renaming duplicate elements as appropriate before proceeding.");
        return sb.toString();
    }

    /**
     * Returns the display alerts for the blocks in this configuration.
     * @return a copy of the alerts defined for the blocks in the configuration for displaying
     */
	public Collection<DisplayAlerts> getDisplayAlerts() {
		return new ArrayList<>(displayAlerts);
	}

	/**
	 * Sets the display values of the alert defined for each block in this configuration.
	 *
	 * @param blocks the blocks based on the configuration
	 */
	protected void setDisplayAlerts(Collection<Block> blocks) {
		// Close old display alerts.
		if (displayAlerts != null) {
			displayAlerts.forEach(DisplayAlerts::close);
		}

		displayAlerts = new ArrayList<>();
		for (Block block : blocks) {
			displayAlerts.add(new DisplayAlerts(block, alertsServer));
		}
	}
	
    /**
     * Returns the top-level alerts settings for all the blocks.
     * @return a copy of the top-level alerts settings for displaying
     */
	public TopLevelAlertSettings getTopLevelAlertSettings() {
		return topLevelAlertSettings;
	}

    /**
     * Returns the top-level alerts settings.
     */
	public void setTopLevelAlertSettings() {
		if (null != topLevelAlertSettings) {
            topLevelAlertSettings.close();
        }
		topLevelAlertSettings = new TopLevelAlertSettings(alertsServer);
	}
}
