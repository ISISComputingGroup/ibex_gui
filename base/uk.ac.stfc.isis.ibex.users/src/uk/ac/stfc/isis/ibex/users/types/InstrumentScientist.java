package uk.ac.stfc.isis.ibex.users.types;

import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;
import uk.ac.stfc.isis.ibex.preferences.Preferences;
import uk.ac.stfc.isis.ibex.users.User;

public class InstrumentScientist extends User {

	private final String password = 
			Preferences.getDefault().getPreferenceStore().getString(PreferenceSupplier.INSTRUMENT_SCIENTIST_PASSWORD);
	
	@Override
	public String name() {
		return "Instrument Scientist";
	}

	@Override
	public String password() {
		return password;
	}
	
	@Override
	public boolean canEditBlocks() {
		return true;
	}
}
