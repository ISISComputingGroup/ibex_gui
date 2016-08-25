package uk.ac.stfc.isis.ibex.experimentdetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A list for UserDetails that can combine users with the same name and role.
 */
public class UserDetailsList extends ArrayList<UserDetails> {
	/**
	 * Constructor that creates a list based on a pre-existing one.
	 * @param values A collection of UserDetails to populate the list with.
	 */
	public UserDetailsList(Collection<UserDetails> values) {
		super(values);
	}

	/**
	 * Default constructor that creates an empty list.
	 */
	public UserDetailsList() {
		super();
	}

	/**
	 *  Modifies the list to assign primary role if same name and role appears multiple times.
	 */
	public void combineSameUsers() {
        List<UserDetails> newList = new ArrayList<UserDetails>();
		
        for (UserDetails oldUser : this) {
        	boolean userAdded = false;
        	for (UserDetails newUser: newList) {
        		if (oldUser.getName().equals(newUser.getName()) && oldUser.getInstitute().equals(newUser.getInstitute())) {
        			newUser.setPrimaryRole(newUser.getRole());
        			userAdded = true;
        		}
        	}
        	if (!userAdded) {
        		newList.add(oldUser);
        	}
		}
        
        this.clear();
        this.addAll(newList);
	}
}
