package uk.ac.stfc.isis.ibex.users.types;

import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;
import uk.ac.stfc.isis.ibex.users.User;

public class Administrator extends User {
	
	@Override
	public String name() {
		return "Administrator";
	}
	
	@Override
	public String password() {
		// As the preference store is not used here, the password
		// is NOT read from the plugin_customization.ini file, so
		// this value cannot be overridden.
		return PreferenceSupplier.administratorPassword();
	}

	@Override
	public boolean canEditBlocks() {
		return true;
	}
}
