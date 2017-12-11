package uk.ac.stfc.isis.ibex.nicos.messages;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.json.JsonDeserialisingConverter;

public class GetScriptStatus extends NICOSMessage<Object> {
	
	public GetScriptStatus() {
        command = "getstatus";
    }

	@Override
	public ReceiveScriptStatus parseResponse(String response) throws ConversionException {
		return new JsonDeserialisingConverter<>(ReceiveScriptStatus.class).convert(response);
	}

}
