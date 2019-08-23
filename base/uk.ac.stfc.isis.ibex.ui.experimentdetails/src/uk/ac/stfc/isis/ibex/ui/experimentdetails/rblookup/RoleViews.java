
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

package uk.ac.stfc.isis.ibex.ui.experimentdetails.rblookup;

import uk.ac.stfc.isis.ibex.experimentdetails.Role;

/**
 * An enum representing the role of a person.
 */
public enum RoleViews {
    
    /**
     * Blank or undefined role, "Any".
     */
	ANY("Any", Role.BLANK),
	
	/**
	 * The person is a contact.
	 */
	CONTACT("Contact", Role.CONTACT),
	
	/**
	 * The person is a PI.
	 */
	PI("PI", Role.PI),
	
	/**
	 * The person is a user.
	 */
	USER("User", Role.USER);
	
	private final String text;
	private final Role modelRole;
	
	/**
	 * Constructor.
	 *
	 * @param text the text
	 * @param modelRole the role
	 */
	RoleViews(final String text, final Role modelRole) {
		this.text = text;
		this.modelRole = modelRole;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	/**
	 * Gets the role of the user.
	 *
	 * @return the role
	 */
	public Role getModelRole() {
		return modelRole;
	}
}
