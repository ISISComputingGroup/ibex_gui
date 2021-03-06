package uk.ac.stfc.isis.ibex.experimentdetails.internal;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.conversion.json.JsonSerialisingConverter;
import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;

/**
 * Wraps the user details list into an object so that it's valid JSON.
 */
public class UserDetailsSerialiser extends Converter<UserDetails[], String> {
    private JsonSerialisingConverter<UserDetailsHolder> userDetailsSerialiser = new JsonSerialisingConverter<UserDetailsHolder>(UserDetailsHolder.class);
    
    private class UserDetailsHolder {
        @SuppressWarnings("unused")
        private UserDetails[] users;
     
        /**
         * To convert into valid JSON the user details list must be inside an object.
         */
        UserDetailsHolder(UserDetails[] userDetails) {
            users = userDetails;
        }
        
    }
    
    @Override
    public String convert(UserDetails[] userDetails) throws ConversionException {
        UserDetailsHolder userDetailsHolder = new UserDetailsHolder(userDetails);
        
        return userDetailsSerialiser.convert(userDetailsHolder);
    }
}
