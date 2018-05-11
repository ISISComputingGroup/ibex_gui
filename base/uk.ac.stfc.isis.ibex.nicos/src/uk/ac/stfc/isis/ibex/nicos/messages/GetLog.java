package uk.ac.stfc.isis.ibex.nicos.messages;

import java.util.Arrays;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.json.JsonDeserialisingConverter;

/**
 * Gets the latest messages from the Nicos console log.
 */
public class GetLog extends NICOSMessage<String> {
	
	/**
     * Gets the last n log messages from Nicos.
     * 
     * @param n
     *            The number of last messages to fetch
     */
    public GetLog(int n) {
        command = "getmessages";
        parameters = Arrays.asList(Integer.toString(n));
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
    public ReceiveLogMessage parseResponse(String response) throws ConversionException {
        JsonDeserialisingConverter<String[][]> deserial = new JsonDeserialisingConverter<>(String[][].class);
        return new ReceiveLogMessage(deserial.convert(response));
	}

}
