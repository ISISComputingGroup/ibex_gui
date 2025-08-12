/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2025
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.alerts;

import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;

/**
 * Used to create the PV addresses for accessing a block's alert settings.
 * 
 */
public class AlertsPVs {
	/**
	 * Enumeration of the PV names used in the alerts system.
	 */
	enum PVName {
		EMAILS("ALERTS:EMAILS:SP"), 
		MOBILES("ALERTS:MOBILES:SP"), 
		MESSAGE("ALERTS:MESSAGE:SP"), 
		ENABLE("AC:ENABLE"), 
		LOW("AC:LOW"), 
		HIGH("AC:HIGH"), 
		DELAY_IN("AC:IN:DELAY"), 
		DELAY_OUT("AC:OUT:DELAY");
		
		// The address of the PV, in fact only the trailing part of the address
		private String address;

		PVName(String address) {
			this.address = address;
		}
		
		/**
		 * Gets the address of the PV.
		 * 
		 * @return the address of the PV
		 */
		public String getAddress() {
			return address;
		}
	}
	
	/**
	 * The root PV address for common alert settings.
	 */
	private final PVAddress commonRoot;

	/**
	 * The root PV address for block-specific alert settings.
	 */
	private final PVAddress blockRoot;

	/**
	 * Constructor.
	 */
	public AlertsPVs() {
		commonRoot = PVAddress.startWith("CS").append("AC");
		blockRoot = PVAddress.startWith("CS").append("SB");
	}

	/**
	 * @return the Emails PV
	 */
	public String getEmailsPv() {
		return commonRoot.endWith(PVName.EMAILS.getAddress());
	}

	/**
	 * @return the Emails PV
	 */
	public String getMobilesPv() {
		return commonRoot.endWith(PVName.MOBILES.getAddress());
	}

	/**
	 * @return the Message PV
	 */
	public String getMessagePv() {
		return commonRoot.endWith(PVName.MESSAGE.getAddress());
	}

	/**
	 * Gets the low limit PV.
	 *
	 * @param blockName the name of the block
	 * @return the low limit PV
	 */
	public String getLowLimitPv(String blockName) {
		return blockRoot.append(blockName).endWith(PVName.LOW.getAddress());
	}

	/**
	 * Gets the high limit PV.
	 *
	 * @param blockName the name of the block
	 * @return the high limit PV
	 */
	public String getHighLimitPv(String blockName) {
		return blockRoot.append(blockName).endWith(PVName.HIGH.getAddress());
	}
	
	/**
	 * Gets the enable PV.
	 *
	 * @param blockName the name of the block
	 * @return the enable PV
	 */
	public String getEnablePv(String blockName) {
		return blockRoot.append(blockName).endWith(PVName.ENABLE.getAddress());
	}

	/**
	 * Gets the Delay In PV.
	 *
	 * @param blockName the name of the block
	 * @return the Delay In PV
	 */
	public String getDelayInPv(String blockName) {
		return blockRoot.append(blockName).endWith(PVName.DELAY_IN.getAddress());
	}

	/**
	 * Gets the Delay Out PV.
	 *
	 * @param blockName the name of the block
	 * @return the Delay Out PV
	 */
	public String getDelayOutPv(String blockName) {
		return blockRoot.append(blockName).endWith(PVName.DELAY_OUT.getAddress());
	}
}
