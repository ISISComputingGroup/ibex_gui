
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
*
* You should have received a copy of the Eclipse Public License v1.0
* along with this program; if not, you can obtain a copy from
* https://www.eclipse.org/org/documents/epl-v10.php or 
* http://opensource.org/licenses/eclipse-1.0.php
*/

package uk.ac.stfc.isis.ibex.configserver.json;

import java.util.Collection;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import uk.ac.stfc.isis.ibex.configserver.BlockRules;
import uk.ac.stfc.isis.ibex.configserver.IocState;
import uk.ac.stfc.isis.ibex.configserver.ServerStatus;
import uk.ac.stfc.isis.ibex.configserver.configuration.Component;
import uk.ac.stfc.isis.ibex.configserver.configuration.ConfigInfo;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.PV;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.configserver.internal.Converters;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Convert;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.json.JsonDeserialisingConverter;
import uk.ac.stfc.isis.ibex.json.JsonSerialisingConverter;
import uk.ac.stfc.isis.ibex.json.LowercaseEnumTypeAdapterFactory;
import uk.ac.stfc.isis.ibex.validators.BlockServerNameValidation;

/**
 * Converters for Json to internal objects.
 */
public class JsonConverters implements Converters {

	@Override
	public Converter<String, Configuration> toConfig() {
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory()).create();
		return new JsonDeserialisingConverter<>(Configuration.class, gson).apply(withFunction(INIT_CONFIG));
	}

	@Override
	public Converter<String, ServerStatus> toServerStatus() {
		return new JsonDeserialisingConverter<>(ServerStatus.class);
	}
	
	@Override
	public Converter<String, BlockRules> toBlockRules() {
		return new JsonDeserialisingConverter<>(BlockRules.class);
	}

    @Override
    public Converter<String, BlockServerNameValidation> toGroupRules() {
        return new JsonDeserialisingConverter<>(BlockServerNameValidation.class);
    }

	@Override
	public Converter<String, Collection<ConfigInfo>> toConfigsInfo() {
		return new JsonDeserialisingConverter<>(ConfigInfo[].class).apply(Convert.<ConfigInfo>toCollection());
	}

	@Override
	public Converter<String, Collection<Component>> toComponents() {
		return new JsonDeserialisingConverter<>(Component[].class)
				.apply(Convert.<Component>toCollection())
				.apply(forEach(INIT_COMP));
	}

	@Override
	public Converter<String, Collection<EditableIoc>> toIocs() {
		return new IocsParametersConverter().apply(new EditableIocsConverter());
	}
	
	@Override
	public Converter<String, Collection<PV>> toPVs() {
		return new JsonDeserialisingConverter<>(String[][].class).apply(new PVsConverter());
	}
	
	@Override
	public Converter<Configuration, String> configToString() {
		return new JsonSerialisingConverter<Configuration>(Configuration.class);
	}
	
	@Override
	public Converter<Collection<String>, String> namesToString() {
		return Convert.toArray(new String[0]).apply(new JsonSerialisingConverter<String[]>(String[].class));
	}
	
	@Override
	public Converter<String, Collection<String>> toNames() {
		return new JsonDeserialisingConverter<>(String[].class).apply(Convert.<String>toCollection());
	}
	
	@Override
	public Converter<String, String> nameToString() {
		return new JsonSerialisingConverter<String>(String.class);
	}
	
	@Override
	public Converter<String, Collection<IocState>> toIocStates() {
		return new IocsParametersConverter().apply(new IocStateConverter());
	}
	
	private static <A, B> Converter<A, B> withFunction(final Function<A, B> function) {
		return new Converter<A, B>() {
			@Override
			public B convert(A value) throws ConversionException {
				return function.apply(value);
			}
		};
	}

	private static <T> Converter<Collection<T>, Collection<T>> forEach(final Function<T, T> function) {
		return new Converter<Collection<T>, Collection<T>>() {
			@Override
			public Collection<T> convert(Collection<T> value) throws ConversionException {
				return Lists.newArrayList(Iterables.transform(value, function));
			}
		};
	}
	
	private static final Function<Configuration, Configuration> INIT_CONFIG = new Function<Configuration, Configuration>() {
		@Override
		public Configuration apply(Configuration uninitialised) {
			return new Configuration(uninitialised);
		}
	};

	private static final Function<Component, Component> INIT_COMP = new Function<Component, Component>() {
		@Override
		public Component apply(Component uninitialised) {
			return new Component(uninitialised);
		}
	};
}
