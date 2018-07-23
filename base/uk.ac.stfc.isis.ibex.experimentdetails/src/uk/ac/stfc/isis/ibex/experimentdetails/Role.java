
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

package uk.ac.stfc.isis.ibex.experimentdetails;

import java.util.Arrays;

/**
 * Enum describing the type of role the user has.
 */
public enum Role {
	/**
	 * The principal investigator for the experiment.
	 */
	PI("PI"),
	/**
	 * A user for the experiment.
	 */
	USER("User"),
	/**
	 * A contact for the experiment.
	 */
	CONTACT("Contact"),
	/**
	 * The user has no role.
	 */
	BLANK("");

	private final String text;

	/**
	 * Creates the role with the associated label representation.
	 * @param text The human readable text to describe the role to the user.
	 */
	Role(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}

	/**
	 * Gets a role based on the string representation of the role.
	 * @param search The string representation.
	 * @return The role enum.
	 */
	public static Role getByString(String search) {
		return Arrays.stream(values())
				.filter((r) -> r.toString().equals(search))
				.findAny()
				.orElse(BLANK);
	}

}
