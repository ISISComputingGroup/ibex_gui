
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

package uk.ac.stfc.isis.ibex.users;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;
import uk.ac.stfc.isis.ibex.preferences.Preferences;
import uk.ac.stfc.isis.ibex.users.types.Administrator;
import uk.ac.stfc.isis.ibex.users.types.DefaultUser;
import uk.ac.stfc.isis.ibex.users.types.InstrumentScientist;

public class Director extends ModelObject {

	private static final Map<String, User> USERS = new HashMap<>();
	static {
		for (User user : allUsers()) {
			USERS.put(user.name(), user);
		}
	};
	
	private User currentUser = initialUser();
	
	private static List<User> allUsers() {
		return Arrays.asList(
				new DefaultUser(), 
				new InstrumentScientist(),
				new Administrator());
	}

	public User currentUser() {
		return currentUser;
	}
	
	public void switchUser(String userName) {
		if (userName.equals(currentUser.name())) {
			return;
		}
		
		if (!USERS.containsKey(userName)) {
			return;
		}
		
		firePropertyChange("currentUser", currentUser, currentUser = USERS.get(userName));
	}	
	
	public Set<String> userNames() {
		return USERS.keySet();
	}
	
	public User userFromName(String name) {
		return USERS.get(name);
	}
	
	private User initialUser() {
		String initialUser = Preferences.getDefault().getPreferenceStore().getString(PreferenceSupplier.INITIAL_USER);
		if (!USERS.containsKey(initialUser) || isAdmin(initialUser)) {
			initialUser = new DefaultUser().name();
		}
		
		return USERS.get(initialUser);
	}

	private boolean isAdmin(String initialUser) {
		return new Administrator().name().equals(initialUser);
	}
}
