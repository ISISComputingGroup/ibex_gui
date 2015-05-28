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
