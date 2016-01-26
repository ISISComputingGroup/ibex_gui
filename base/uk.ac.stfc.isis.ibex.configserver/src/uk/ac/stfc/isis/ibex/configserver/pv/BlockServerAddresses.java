
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

public class BlockServerAddresses {

	private static final String SERVER_STATUS = "SERVER_STATUS"; 
	private static final String GET_CURRENT_CONFIG_DETAILS = "GET_CURR_CONFIG_DETAILS";
	private static final String GET_CONFIG_DETAILS = "GET_CONFIG_DETAILS";
	private static final String SET_CURRENT_CONFIG = "SET_CURR_CONFIG_DETAILS";
	private static final String LOAD_CONFIG = "LOAD_CONFIG";
	private static final String SAVE_CONFIG = "SAVE_CONFIG";
	private static final String SAVE_NEW_CONFIG = "SAVE_NEW_CONFIG";
	private static final String CONFIGS = "CONFIGS";
	private static final String IOCS = "IOCS";
	private static final String IOCS_NOT_TO_STOP = "IOCS_NOT_TO_STOP";
	private static final String ALL_PVS = "PVS:ALL";	
	private static final String HIGH_INTEREST_PVS = "PVS:INTEREST:HIGH";
	private static final String MEDIUM_INTEREST_PVS = "PVS:INTEREST:MEDIUM";
    private static final String FACILITY_INTEREST_PVS = "PVS:INTEREST:FACILITY";
	private static final String ACTIVE_PVS = "PVS:ACTIVE";
	private static final String COMPONENTS = "COMPS";
	private static final String SAVE_COMPONENT = "SAVE_NEW_COMPONENT";
	private static final String GET_COMPONENT = "GET_COMPONENT_DETAILS";
	private static final String DELETE_CONFIGS = "DELETE_CONFIGS";
	private static final String DELETE_COMPONENTS = "DELETE_COMPONENTS";
	private static final String BLANK_CONFIG = "BLANK_CONFIG";
	private static final String BLOCK_RULES = "BLOCK_RULES";	
	
	private static final String START_IOCS = "START_IOCS";
	private static final String STOP_IOCS = "STOP_IOCS";
	private static final String RESTART_IOCS = "RESTART_IOCS";
	
	private static final String DESCRIPTION_FIELD = ".DESC";
	
	private final PVAddress blockServerAddress;
	private final PVAddress blockAlias;
	
	public BlockServerAddresses() {
		blockServerAddress = PVAddress.startWith("CS").append("BLOCKSERVER");
		blockAlias = PVAddress.startWith("CS").append("SB");
	}
	
	public String serverStatus() {
		return blockServerAddress.endWith(SERVER_STATUS);
	}
	
	public String currentConfig() {
		return blockServerAddress.endWith(GET_CURRENT_CONFIG_DETAILS);
	}
	
	public String blankConfig() {
		return blockServerAddress.endWith(BLANK_CONFIG);
	}
	
	public String config(String configName) {
		return blockServerAddress.append(configName).endWith(GET_CONFIG_DETAILS);
	}
	
	public String configs() {
		return blockServerAddress.endWith(CONFIGS);
	}
	
	public String getConfig() {
		return blockServerAddress.endWith(GET_CONFIG_DETAILS);
	}
	
	public String component(String name) {	
		return blockServerAddress.append(name).endWith(GET_COMPONENT);
	}
	
	public String setCurrentConfig() {
		return blockServerAddress.endWith(SET_CURRENT_CONFIG);
	}

	public String loadConfig() {
		return blockServerAddress.endWith(LOAD_CONFIG);
	}
	
	public String saveConfig() {
		return blockServerAddress.endWith(SAVE_CONFIG);
	}
	
	public String saveNewConfig() {
		return blockServerAddress.endWith(SAVE_NEW_CONFIG);
	}
	
	public String iocs() {
		return blockServerAddress.endWith(IOCS);
	}

	public String iocsNotToStop() {
		return blockServerAddress.endWith(IOCS_NOT_TO_STOP);
	}

	public String pvs() {
		return blockServerAddress.endWith(ALL_PVS);
	}
	
	public String highInterestPVs() {
		return blockServerAddress.endWith(HIGH_INTEREST_PVS);
	}
	
    public String mediumInterestPVs() {
		return blockServerAddress.endWith(MEDIUM_INTEREST_PVS);
	}
	
    public String facilityInterestPVs() {
        return blockServerAddress.endWith(FACILITY_INTEREST_PVS);
    }

	public String activePVs() {
		return blockServerAddress.endWith(ACTIVE_PVS);
	}
	
	public String blockAlias(String blockName) {
		return blockAlias.endWith(blockName);
	}
	
	public String blockDescription(String pvAddress) {
		return pvAddress + DESCRIPTION_FIELD;
	}
	
	public String components() {
		return blockServerAddress.endWith(COMPONENTS);
	}
	
	public String saveComponent() {
		return blockServerAddress.endWith(SAVE_COMPONENT);
	}
	
	public String deleteConfigs() {
		return blockServerAddress.endWith(DELETE_CONFIGS);
	}
	
	public String deleteComponents() {
		return blockServerAddress.endWith(DELETE_COMPONENTS);
	}
	
	public String startIocs() {
		return blockServerAddress.endWith(START_IOCS);
	}
	
	public String stopIocs() {
		return blockServerAddress.endWith(STOP_IOCS);
	}
	
	public String restartIocs() {
		return blockServerAddress.endWith(RESTART_IOCS);
	}
	
	public String blockRules() {
		return blockServerAddress.endWith(BLOCK_RULES);
	}
}
