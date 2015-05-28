package uk.ac.stfc.isis.ibex.experimentdetails.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;

public class ParametersConverter extends Converter<String, Collection<String>> {

	@Override
	public Collection<String> convert(String value) throws ConversionException {
		List<String> pvs = new ArrayList<>();
		for (String pv : removeBracesAndSplit(value)){
			pvs.add(unquote(pv));
		}
		return pvs;
	}

	private String unquote(String pv) {
		return pv.replaceAll("\"","");
	}

	private String[] removeBracesAndSplit(String value) {
		return value.substring(1, value.length() - 2).split(", ");
	}
}
