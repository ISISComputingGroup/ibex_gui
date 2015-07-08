
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

package uk.ac.stfc.isis.ibex.users.ui;

import java.util.ArrayList;
import java.util.List;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.users.Director;
import uk.ac.stfc.isis.ibex.users.User;

public class UserSwitcherModel extends ModelObject {
	
	private final Director director;
	private String selectedUserName;
	private boolean passwordRequired;
	private String password;
	private boolean isValid;
	
	private User selectedUser;
	
	public UserSwitcherModel(Director director) {
		this.director = director;
		setSelectedUserName(director.currentUser().name());
		setPassword(director.currentUser().password());
		passwordRequired = director.currentUser().requiresPassword();
	}
	
	public String currentUserName() {
		return director.currentUser().name();
	}
	
	public String selectedUserName() {
		return selectedUserName;
	}
	
	public String getPassword() {
		return password;
	}
		
	public void setPassword(String password) {
		firePropertyChange("password", this.password, this.password = password);
		firePropertyChange("isValid", isValid, isValid = isValid());
	}
	
	public boolean getPasswordRequired() {
		return passwordRequired;
	}
	
	public boolean getIsValid() {
		return isValid;
	}
	
	private boolean isValid() {
		return !passwordRequired || checkPassword(password);
	}
	
	public void setSelectedUserName(String userName) {
		User newUser = director.userFromName(userName);
		if (newUser == null) {
			return;
		}
		
		selectedUser = newUser;
		firePropertyChange("selectedUserName", selectedUserName, selectedUserName = userName);
		firePropertyChange("passwordRequired", passwordRequired, passwordRequired = newUser.requiresPassword());
		firePropertyChange("isValid", isValid, isValid = isValid());
	}
	
	public List<String> userNames() {
		return new ArrayList<>(director.userNames());
	}
	
	private boolean checkPassword(String password) {
		return selectedUser.password().equals(password);
	}
}
