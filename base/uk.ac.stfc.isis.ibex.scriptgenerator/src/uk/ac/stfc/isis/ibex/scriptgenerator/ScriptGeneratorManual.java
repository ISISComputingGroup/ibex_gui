package uk.ac.stfc.isis.ibex.scriptgenerator;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;

/**
 * A wrapper for getting the user manual URL. 
 *
 */
public final class ScriptGeneratorManual {
	
	private ScriptGeneratorManual() {
		// not called
	}
	
	/**
	 * The time to wait before timing out from trying to connect to the manual.
	 */
	private static final int URL_TIMEOUT_MILLISECONDS = 3000;
	
	/**
	 * A response code for a get call that is good.
	 */
	private static final int GOOD_RESPONSE_CODE = 200;

	/**
	 * A response code for a get call that is bad.
	 */
	private static final int BAD_RESPONSE_CODE = 300;
	
	private static final Logger LOG = IsisLog.getLogger(ScriptGeneratorManual.class);
	
	/**
	 * SHOULD ONLY BE CALLED IN ANOTHER THREAD. Load the URL for the user manual
	 * from preferences and attempt to connect to them. If we can connect with them
	 * then select this as the url for the manual or an empty optional if we can't
	 * connect to any.
	 * 
	 * @return An empty optional if there is no preference that we can connect to,
	 *         or an optional containing the url.
	 */
	public static Optional<URL> getUserManualUrl() {
		
		var preferenceSupplier = new PreferenceSupplier();

	    String preferenceProperty = preferenceSupplier.scriptGeneratorManualURL();
	    
	    // Loop through all URLs in the preference property
	    // and return the first one reachable from the user's network
	    for (String url : preferenceProperty.split(",")) {
	        try {
	            URL possibleUrl = new URI(url).toURL();
	            HttpURLConnection connection = (HttpURLConnection) possibleUrl.openConnection();
	            connection.setConnectTimeout(URL_TIMEOUT_MILLISECONDS);
	            connection.setRequestMethod("GET");
	            connection.connect();
	            int responseCode = connection.getResponseCode();
	            if (responseCode >= GOOD_RESPONSE_CODE && responseCode < BAD_RESPONSE_CODE) {
	                return Optional.of(possibleUrl);
	            }
	        } catch (IOException | URISyntaxException ex) {
	            LOG.debug("Invalid URL for user manual was found: " + url);
	        }
	    }
	    
	    LOG.warn("No valid URLs for the user manual were found");
	    return Optional.empty();
	}

}
