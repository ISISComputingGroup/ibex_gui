package uk.ac.stfc.isis.ibex.experimentdetails.internal;

import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;
import uk.ac.stfc.isis.ibex.json.JsonSerialisingConverter;

public class UserDetailsSerialiser extends JsonSerialisingConverter<UserDetails[]> {

	public UserDetailsSerialiser() {
		super(UserDetails[].class);
	}
}
