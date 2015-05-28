package uk.ac.stfc.isis.ibex.json;

import java.beans.PropertyChangeSupport;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonSerialisingConverter<T> extends Converter<T, String> {

	private final static ExclusionStrategy EXCLUDE_PROPERTY_CHANGE_SUPPORT = 
			new SpecificClassExclusionStrategy(PropertyChangeSupport.class);
	
	private final Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory())
			.setExclusionStrategies(EXCLUDE_PROPERTY_CHANGE_SUPPORT)
			.create();

	private final Class<T> classOfT;
	
	public JsonSerialisingConverter(Class<T> classOfT) {
		this.classOfT = classOfT;
	}
	
	@Override
	public String convert(T value) throws ConversionException {
		return gson.toJson(value, classOfT);
	}
}
