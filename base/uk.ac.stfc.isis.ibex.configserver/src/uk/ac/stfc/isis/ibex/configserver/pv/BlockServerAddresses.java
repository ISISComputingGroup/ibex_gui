
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

package uk.ac.stfc.isis.ibex.configserver.pv;

import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;

/**
 * Addresses for PVs within the block server
 */
public class BlockServerAddresses {

    /** PV ending for server status. */
    private static final String SERVER_STATUS = "SERVER_STATUS";
    /** PV ending for retrieving the current configuration details. */
	private static final String GET_CURRENT_CONFIG_DETAILS = "GET_CURR_CONFIG_DETAILS";
    /**
     * PV ending for retrieving the configuration details for a named
     * configuration.
     */
	private static final String GET_CONFIG_DETAILS = "GET_CONFIG_DETAILS";
    /** PV ending for setting the current configuration details. */
	private static final String SET_CURRENT_CONFIG = "SET_CURR_CONFIG_DETAILS";
    /** PV ending for loading a named configuration. */
	private static final String LOAD_CONFIG = "LOAD_CONFIG";
    /** PV ending for saving an existing configuration. */
	private static final String SAVE_CONFIG = "SAVE_CONFIG";
    /** PV ending for saving a new configuration. */
	private static final String SAVE_NEW_CONFIG = "SAVE_NEW_CONFIG";
    /** PV ending for retrieving a list of configuration names. */
	private static final String CONFIGS = "CONFIGS";
    /**
     * PV ending for retrieving the IOCs associated with the current
     * configuration.
     */
	private static final String IOCS = "IOCS";
    /**
     * PV ending for retrieving the IOCs associated with the current
     * configuration that should not be stopped when the configuration is
     * loaded.
     */
	private static final String IOCS_NOT_TO_STOP = "IOCS_NOT_TO_STOP";
    /**
     * PV ending for retrieving all PVs available under the current
     * configuration.
     */
    private static final String ALL_PVS = "PVS:ALL";
    /** PV ending for retrieving all high interest PVs. */
	private static final String HIGH_INTEREST_PVS = "PVS:INTEREST:HIGH";
    /** PV ending for retrieving all medium interest PVs. */
	private static final String MEDIUM_INTEREST_PVS = "PVS:INTEREST:MEDIUM";
    /** PV ending for retrieving all facility PVs. */
    private static final String FACILITY_INTEREST_PVS = "PVS:INTEREST:FACILITY";
    /** PV ending for retrieving all active PVs. */
	private static final String ACTIVE_PVS = "PVS:ACTIVE";
    /** PV ending for retrieving the names of all existing components. */
	private static final String COMPONENTS = "COMPS";
    /** PV ending for saving a new component. */
	private static final String SAVE_COMPONENT = "SAVE_NEW_COMPONENT";
    /** PV ending for retrieving the details for a named component. */
	private static final String GET_COMPONENT = "GET_COMPONENT_DETAILS";
    /** PV ending for deleting named configurations. */
	private static final String DELETE_CONFIGS = "DELETE_CONFIGS";
    /** PV ending for deleting named components. */
	private static final String DELETE_COMPONENTS = "DELETE_COMPONENTS";
    /** PV ending for retrieving an empty configuration. */
	private static final String BLANK_CONFIG = "BLANK_CONFIG";
    /** PV ending for block rules. */
    private static final String BLOCK_RULES = "BLOCK_RULES";
    /** PV ending for group rules. */
    private static final String GROUP_RULES = "GROUP_RULES";
    /** PV ending for configuration description rules. */
    private static final String CONFIG_DESCRIPTION_RULES = "CONF_DESC_RULES";

    /** PV ending for starting named IOCs. */
	private static final String START_IOCS = "START_IOCS";
    /** PV ending for stopping named IOCs. */
	private static final String STOP_IOCS = "STOP_IOCS";
    /** PV ending for restarting named IOCs. */
	private static final String RESTART_IOCS = "RESTART_IOCS";
	
    /** The field used to get the description from a PV. */
	private static final String DESCRIPTION_FIELD = ".DESC";
    /** The field used to get the alarm severity from a PV. */
    private static final String ALARM_FIELD = ".SEVR";

    /** The field used to get the current banner description. */
    private static final String BANNER_DESCRIPTION = "BANNER_DESCRIPTION";
	
    /** The PV address of the block server. */
	private final PVAddress blockServerAddress;
    /** The PV address for block server block aliases. */
	private final PVAddress blockAlias;

    /** Default constructor. */
	public BlockServerAddresses() {
		blockServerAddress = PVAddress.startWith("CS").append("BLOCKSERVER");
		blockAlias = PVAddress.startWith("CS").append("SB");
	}
	
    /**
     * @return The PV address to get the current server status
     */
	public String serverStatus() {
		return blockServerAddress.endWith(SERVER_STATUS);
	}

    /**
     * @return The PV address to get the current configuration name
     */
	public String currentConfig() {
		return blockServerAddress.endWith(GET_CURRENT_CONFIG_DETAILS);
	}

    /**
     * @return The PV address to get a blank configuration
     */
	public String blankConfig() {
		return blockServerAddress.endWith(BLANK_CONFIG);
	}
	
    /**
     * @param configName The name of the configuration to get details for
     * @return The PV address to get the configuration details for a named
     *         configuration
     */
	public String config(String configName) {
		return blockServerAddress.append(configName).endWith(GET_CONFIG_DETAILS);
	}
	
    /**
     * @return The PV address to get the list of existing configuration names
     */
	public String configs() {
		return blockServerAddress.endWith(CONFIGS);
	}
	
    /**
     * @return The PV address to get all of the existing configuration details
     */
	public String getConfig() {
		return blockServerAddress.endWith(GET_CONFIG_DETAILS);
	}
	
    /**
     * @param name The name of the component to be retrieved
     * @return The PV address to retrieve the named component
     */
	public String component(String name) {	
		return blockServerAddress.append(name).endWith(GET_COMPONENT);
	}
	
    /**
     * @return The PV address to set the current configuration
     */
	public String setCurrentConfig() {
		return blockServerAddress.endWith(SET_CURRENT_CONFIG);
	}

    /**
     * @return The PV address to reload the current configuration
     */
	public String loadConfig() {
		return blockServerAddress.endWith(LOAD_CONFIG);
	}
	
    /**
     * @return The PV address to save the current configuration
     */
	public String saveConfig() {
		return blockServerAddress.endWith(SAVE_CONFIG);
	}
	
    /**
     * @return The PV address to save a new configuration
     */
	public String saveNewConfig() {
		return blockServerAddress.endWith(SAVE_NEW_CONFIG);
	}
	
    /**
     * @return The PV address to retrieve IOCs associated with the current
     *         configuration
     */
	public String iocs() {
		return blockServerAddress.endWith(IOCS);
	}

    /**
     * @return The PV address to retrieve a list of IOCs that shouldn't be
     *         stopped under the current configuration
     */
	public String iocsNotToStop() {
		return blockServerAddress.endWith(IOCS_NOT_TO_STOP);
	}

    /**
     * @return The PV to retrieve a list of all available PVs
     */
	public String pvs() {
		return blockServerAddress.endWith(ALL_PVS);
	}
	
    /**
     * @return The PV address to retrieve a list of all high interest PVs
     */
	public String highInterestPVs() {
		return blockServerAddress.endWith(HIGH_INTEREST_PVS);
	}

    /**
     * @return The PV address to retrieve a list of all medium interest PVs
     */
    public String mediumInterestPVs() {
		return blockServerAddress.endWith(MEDIUM_INTEREST_PVS);
	}

    /**
     * @return The PV address to retrieve a list of all facility PVs
     */
    public String facilityInterestPVs() {
        return blockServerAddress.endWith(FACILITY_INTEREST_PVS);
    }

    /**
     * @return The PV address to retrieve a list of all active PVs
     */
	public String activePVs() {
		return blockServerAddress.endWith(ACTIVE_PVS);
	}
	
    /**
     * @param blockName The block name to get the PV for
     * @return The PV for the named block
     */
	public String blockAlias(String blockName) {
		return blockAlias.endWith(blockName);
	}
	
    /**
     * @param pvAddress The PV address to append the description field to
     * @return The address for the description field of the input PV
     */
	public String blockDescription(String pvAddress) {
		return pvAddress + DESCRIPTION_FIELD;
	}

    /**
     * Get the address of the PV holding the banner description.
     * 
     * @return the banner description
     */
	public String bannerDescription() {
		return blockServerAddress.endWith(BANNER_DESCRIPTION);
	}

    /**
     * Given a block's address, gets the address of its alarm field.
     * 
     * @param pvAddress the PV address of a block
     * @return the alarm severity field of a given PV address.
     */
    public String blockAlarm(String pvAddress) {
        return pvAddress + ALARM_FIELD;
    }

    /**
     * @return The PV to retrieve a list of the existing component names
     */
	public String components() {
		return blockServerAddress.endWith(COMPONENTS);
	}
	
    /**
     * @return The PV to save a component
     */
	public String saveComponent() {
		return blockServerAddress.endWith(SAVE_COMPONENT);
	}
	
    /**
     * @return The PV to delete specified configurations
     */
	public String deleteConfigs() {
		return blockServerAddress.endWith(DELETE_CONFIGS);
	}
	
    /**
     * @return The PV to delete specified components
     */
	public String deleteComponents() {
		return blockServerAddress.endWith(DELETE_COMPONENTS);
	}
	
    /**
     * @return The PV to start specified IOCs
     */
	public String startIocs() {
		return blockServerAddress.endWith(START_IOCS);
	}

    /**
     * @return The PV to stop specified IOCs
     */
	public String stopIocs() {
		return blockServerAddress.endWith(STOP_IOCS);
	}

    /**
     * @return The PV to restart specified IOCs
     */
	public String restartIocs() {
		return blockServerAddress.endWith(RESTART_IOCS);
	}
	
    /**
     * @return address for block rules on the block server
     */
	public String blockRules() {
		return blockServerAddress.endWith(BLOCK_RULES);
	}

    /**
     * @return address for group rules on the block server
     */
    public String groupRules() {
        return blockServerAddress.endWith(GROUP_RULES);
    }

    /**
     * @return address for configuration description rules on the block server
     */
    public String configDescritpionRules() {
        return blockServerAddress.endWith(CONFIG_DESCRIPTION_RULES);
    }

}
