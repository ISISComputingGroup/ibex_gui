package uk.ac.stfc.isis.ibex.experimentdetails.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;
import uk.ac.stfc.isis.ibex.json.JsonDeserialisingConverter;
import uk.ac.stfc.isis.ibex.json.LowercaseEnumTypeAdapterFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserDetailsConverter extends
		Converter<String, Collection<UserDetails>> {
	
	private final Gson gson = new GsonBuilder().registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory()).create();

	@Override
	public Collection<UserDetails> convert(String value)
			throws ConversionException {
		Converter<String, UserDetails[]> jsonConverter = new JsonDeserialisingConverter<>(UserDetails[].class, gson);
		UserDetails[] parsed = jsonConverter.convert(value);
		
		List<UserDetails> userDetails = new ArrayList<>();
		
		for (UserDetails user : parsed) {
			userDetails.add(new UserDetails(user));
		}
		
		return userDetails;
	}

}
