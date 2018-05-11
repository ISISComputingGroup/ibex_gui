package uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.json.JsonDeserialisingConverter;
import uk.ac.stfc.isis.ibex.nicos.messages.NICOSMessage;

/**
 * Gets the script status from the server.
 */
public class GetScriptStatus extends NICOSMessage<Object> {
	
	/**
	 * Gets the script status from the server.
	 */
	public GetScriptStatus() {
        command = "getstatus";
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReceiveScriptStatus parseResponse(String response) throws ConversionException {
		return new JsonDeserialisingConverter<>(ReceiveScriptStatus.class).convert(response);
	}

}
