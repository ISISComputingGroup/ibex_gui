package uk.ac.stfc.isis.ibex.configserver.internal;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.IocState;
import uk.ac.stfc.isis.ibex.configserver.ServerStatus;
import uk.ac.stfc.isis.ibex.configserver.configuration.Component;
import uk.ac.stfc.isis.ibex.configserver.configuration.ConfigInfo;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.PV;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;

public interface Converters {

	Converter<String, Configuration> toConfig();

	Converter<String, ServerStatus> toServerStatus();

	Converter<String, Collection<ConfigInfo>> toConfigsInfo();

	Converter<String, Collection<Component>> toComponents();

	Converter<String, Collection<EditableIoc>> toIocs();

	Converter<String, Collection<PV>> toPVs();

	Converter<Configuration, String> configToString();

	Converter<Collection<String>, String> namesToString();

	Converter<String, String> nameToString();

	Converter<String, Collection<IocState>> toIocStates();

	Converter<String, Collection<String>> toNames();
}
