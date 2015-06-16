package uk.ac.stfc.isis.ibex.configserver.json;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.configserver.internal.IocParameters;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class EditableIocsConverter extends Converter<Map<String, IocParameters>, Collection<EditableIoc>> {
	
	@Override
	public Collection<EditableIoc> convert(Map<String, IocParameters> value) throws ConversionException {
		return Lists.newArrayList(Iterables.transform(value.entrySet(), new Function<Map.Entry<String, IocParameters>, EditableIoc>() {

			@Override
			public EditableIoc apply(Entry<String, IocParameters> entry) {
				String name = entry.getKey();
				IocParameters parameters = entry.getValue();
				
				EditableIoc ioc = new EditableIoc(name);
				if (parameters.getMacros() != null) {
					ioc.setAvailableMacros(parameters.getMacros());
				}
				
				if (parameters.getPVs() != null) {
					ioc.setAvailablePVs(parameters.getPVs());
				}
				
				if (parameters.getPVSets() != null) {
					ioc.setAvailablePVSets(parameters.getPVSets());
				}
				
				return ioc;				
			}
		}));
	}
}
