package uk.ac.stfc.isis.ibex.nicos.messages;

import java.util.Arrays;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.json.JsonDeserialisingConverter;

/**
 * Gets the latest messages from the Nicos console log.
 */
public class GetConsoleLog extends NICOSMessage<String> {
	
	/**
     * Gets the last n log messages from Nicos.
     * 
     * @param n
     *            The number of last messages to fetch
     */
    public GetConsoleLog(String n) {
        command = "getmessages";
        parameters = Arrays.asList(n);
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
    public ReceiveConsoleLogMessage parseResponse(String response) throws ConversionException {
        JsonDeserialisingConverter<String[][]> deserial = new JsonDeserialisingConverter<>(String[][].class);
        return new ReceiveConsoleLogMessage(deserial.convert(response));
	}

}
