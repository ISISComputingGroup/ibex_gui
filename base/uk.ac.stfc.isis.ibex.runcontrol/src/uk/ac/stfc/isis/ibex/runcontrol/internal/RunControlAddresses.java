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

package uk.ac.stfc.isis.ibex.runcontrol.internal;

import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;

/**
 * Used to create the PV addresses for accessing a block's run-control settings.
 * 
 */
public class RunControlAddresses {
	private static final String LOW_LIMIT = "RC:LOW";
	private static final String HIGH_LIMIT = "RC:HIGH";
	private static final String SUSPEND_ON_INVALID = "RC:SOI";
	private static final String ENABLE = "RC:ENABLE";
	private static final String INRANGE = "RC:INRANGE";

	private final PVAddress blockRoot;

	/**
	 * Constructor.
	 */
	public RunControlAddresses() {
		blockRoot = PVAddress.startWith("CS").append("SB");
	}

	/**
	 * Gets the low limit PV.
	 *
	 * @param blockName the name of the block
	 * @return the low limit PV
	 */
	public String getLowLimitPv(String blockName) {
		return blockRoot.append(blockName).endWith(LOW_LIMIT);
	}

	/**
	 * Gets the high limit PV.
	 *
	 * @param blockName the name of the block
	 * @return the high limit PV
	 */
	public String getHighLimitPv(String blockName) {
		return blockRoot.append(blockName).endWith(HIGH_LIMIT);
	}
	
	/**
	 * Gets the suspend on invalid PV.
	 *
	 * @param blockName the name of the block
	 * @return the high limit PV
	 */
	public String getSuspendOnInvalidPv(String blockName) {
		return blockRoot.append(blockName).endWith(SUSPEND_ON_INVALID);
	}

	/**
	 * Gets the enable PV.
	 *
	 * @param blockName the name of the block
	 * @return the enable PV
	 */
	public String getEnablePv(String blockName) {
		return blockRoot.append(blockName).endWith(ENABLE);
	}

	/**
	 * Gets the in range PV.
	 *
	 * @param blockName the name of the block
	 * @return the in range PV
	 */
	public String getInRangePv(String blockName) {
		return blockRoot.append(blockName).endWith(INRANGE);
	}
}
