package uk.ac.stfc.isis.ibex.users.types;

import uk.ac.stfc.isis.ibex.users.User;

public class DefaultUser extends User {

	@Override
	public String name() {
		return "Default user";
	}

	@Override
	public boolean canEditBlocks() {
		return false;
	}
}
