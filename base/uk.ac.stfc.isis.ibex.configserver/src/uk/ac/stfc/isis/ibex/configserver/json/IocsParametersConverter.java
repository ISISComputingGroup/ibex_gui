package uk.ac.stfc.isis.ibex.configserver.json;

import java.lang.reflect.Type;
import java.util.Map;

import uk.ac.stfc.isis.ibex.configserver.internal.IocParameters;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class IocsParametersConverter extends Converter<String, Map<String, IocParameters>> {

	private final Gson gson = new Gson();

	// e.g. {"MOTORSIM": {"running": false}, "TPG300_01": {"running": false} }
	private static final Type SERVER_IOC_DATA_FORMAT = new TypeToken<Map<String, IocParameters>>(){}.getType();

	@Override
	public Map<String, IocParameters> convert(String json) throws ConversionException {
		try {
			return gson.fromJson(json, SERVER_IOC_DATA_FORMAT);
		} catch (JsonSyntaxException e) {
			throw new ConversionException("Error parsing json", e);
		}
	}
}
