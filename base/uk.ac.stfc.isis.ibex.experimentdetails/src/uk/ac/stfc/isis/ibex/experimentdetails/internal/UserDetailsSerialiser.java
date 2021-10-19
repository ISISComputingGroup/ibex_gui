package uk.ac.stfc.isis.ibex.experimentdetails.internal;

import java.util.function.Function;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.json.JsonSerialisingConverter;
import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;

/**
 * Wraps the user details list into an object so that it's valid JSON.
 */
public class UserDetailsSerialiser implements Function<UserDetails[], String> {
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
    public String apply(UserDetails[] userDetails) throws ConversionException {
        UserDetailsHolder userDetailsHolder = new UserDetailsHolder(userDetails);
        
        return userDetailsSerialiser.apply(userDetailsHolder);
    }
}
