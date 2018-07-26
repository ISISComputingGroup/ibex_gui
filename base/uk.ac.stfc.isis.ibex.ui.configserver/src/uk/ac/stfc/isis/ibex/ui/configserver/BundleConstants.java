/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2017 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.configserver;

/**
 * The Class BundleConstants contains constants needed for this bundle.
 */
public final class BundleConstants {

	private static final String BUNDLE_NAME = "uk.ac.stfc.isis.ibex.ui.configserver";

	private BundleConstants() {
		
	}

	/**
	 * Gets the platform plugin text string.
	 *
	 * @return the platform plugin
	 */
	public static String getPlatformPlugin() {
		return "platform:/plugin/" + BUNDLE_NAME;
	}
	

	/**
	 * Gets the class URI as a bundleclass.
	 *
	 * @param <T> the generic type type of class to get name for
	 * @param clazz the clazz the class passed
	 * @return the class URI
	 */
	public static <T> String getClassURI(Class<T> clazz) {

		return "bundleclass://" + BUNDLE_NAME + "/" + clazz.getName();
	}

}
