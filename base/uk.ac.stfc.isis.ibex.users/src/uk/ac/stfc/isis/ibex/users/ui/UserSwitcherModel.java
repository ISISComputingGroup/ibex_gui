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
