package uk.ac.stfc.isis.ibex.configserver.json;

import java.util.Arrays;
import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.configuration.PV;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class PVsConverter extends Converter<String[][], Collection<PV>> {

	@Override
	public Collection<PV> convert(String[][] value) throws ConversionException {
		return Lists.newArrayList(Iterables.transform(Arrays.asList(value), new Function<String[], PV>() {
			@Override
			public PV apply(String[] info) {
				return new PV(info[0], info[1], info[2], info[3]);
			}
		}));
	}
}
