package uk.ac.stfc.isis.ibex.json;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class JsonDeserialisingConverter<T> extends Converter<String, T>{

	protected final Gson gson;
	private final Class<T> classOfT;

	public JsonDeserialisingConverter(Class<T> classOfT, Gson gson) {
		this.gson = gson;
		this.classOfT = classOfT;
	}
	
	public JsonDeserialisingConverter(Class<T> outputType) {
		this(outputType, new Gson());
	}
	
	@Override
	public T convert(String value) throws ConversionException {
		try {
			// NB. Gson uses reflection to initialise the internal
			//     fields of the class: the returned class will not 
			//     have been initialised through its constructor.
			return parseJson(value);
		} catch (JsonSyntaxException e) {
			throw new ConversionException("Error parsing json", e);
		}
	}
	
	protected T parseJson(String json) throws JsonSyntaxException {
		return gson.fromJson(json, classOfT);
	}
}
