package uk.ac.stfc.isis.ibex.configserver.json;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import uk.ac.stfc.isis.ibex.configserver.IocState;
import uk.ac.stfc.isis.ibex.configserver.internal.IocParameters;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class IocStateConverter extends Converter<Map<String, IocParameters>, Collection<IocState>> {
	
	@Override
	public Collection<IocState> convert(Map<String, IocParameters> value) throws ConversionException {
		return Lists.newArrayList(Iterables.transform(value.entrySet(), new Function<Map.Entry<String, IocParameters>, IocState>() {
			@Override
			public IocState apply(Entry<String, IocParameters> entry) {
				String name = entry.getKey();
				IocParameters parameters = entry.getValue();
				
				return 	new IocState(name, parameters.isRunning(), "", true);
			}
		}));
	}
}
