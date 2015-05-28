package uk.ac.stfc.isis.ibex.configserver.internal;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.EditableIocState;
import uk.ac.stfc.isis.ibex.configserver.IocState;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class IocStateEditingConverter extends Converter<Collection<IocState>, Collection<EditableIocState>> {
	
	private final ConfigServer server;

	public IocStateEditingConverter(ConfigServer server) {
		this.server = server;
	}
	
	@Override
	public Collection<EditableIocState> convert(Collection<IocState> value)
			throws ConversionException {
		return Lists.newArrayList(Iterables.transform(value, new Function<IocState, EditableIocState>() {
			@Override
			public EditableIocState apply(IocState state) {
				return new EditableIocState(
						state, 
						server.iocDescription(state.getName()), 
						server.iocDescriptionSetter(state.getName()));
			}
		}));
	}

}
