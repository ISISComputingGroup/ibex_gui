
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

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

import uk.ac.stfc.isis.ibex.configserver.BlockRules;
import uk.ac.stfc.isis.ibex.configserver.IocState;
import uk.ac.stfc.isis.ibex.configserver.ServerStatus;
import uk.ac.stfc.isis.ibex.configserver.configuration.CustomBannerData;
import uk.ac.stfc.isis.ibex.configserver.configuration.ComponentInfo;
import uk.ac.stfc.isis.ibex.configserver.configuration.ConfigInfo;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.PV;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.configserver.internal.Converters;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Convert;
import uk.ac.stfc.isis.ibex.epics.conversion.json.JsonDeserialisingConverter;
import uk.ac.stfc.isis.ibex.epics.conversion.json.JsonSerialisingConverter;
import uk.ac.stfc.isis.ibex.validators.BlockServerNameValidator;

/**
 * Converters for Json to internal objects.
 */
public class JsonConverters implements Converters {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Function<String, Configuration> toConfig() {
		return new JsonDeserialisingConverter<>(Configuration.class).andThen(withFunction(INIT_CONFIG));
	}

	/**
	 * {@inheritDoc}
	 */
    @Override
    public Function<String, Collection<Configuration>> toConfigList() {
        return new JsonDeserialisingConverter<>(Configuration[].class)
                .andThen(Arrays::asList).andThen(forEach(INIT_CONFIG));
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
	public Function<String, ServerStatus> toServerStatus() {
		return new JsonDeserialisingConverter<>(ServerStatus.class);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Function<String, BlockRules> toBlockRules() {
		return new JsonDeserialisingConverter<>(BlockRules.class);
	}

	/**
	 * {@inheritDoc}
	 */
    @Override
    public Function<String, BlockServerNameValidator> toBlockServerTextValidor() {
        return new JsonDeserialisingConverter<>(BlockServerNameValidator.class);
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
	public Function<String, Collection<ConfigInfo>> toConfigsInfo() {
		return new JsonDeserialisingConverter<>(ConfigInfo[].class).andThen(Arrays::asList);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Function<String, Collection<ComponentInfo>> toComponents() {
		return new JsonDeserialisingConverter<>(ComponentInfo[].class)
				.andThen(Arrays::asList)
				.andThen(forEach(INIT_COMP));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Function<String, Collection<EditableIoc>> toIocs() {
		return new IocsParametersConverter().andThen(new EditableIocsConverter());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Function<String, Collection<PV>> toPVs() {
		return new JsonDeserialisingConverter<>(String[][].class).andThen(new PVsConverter());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Function<Configuration, String> configToString() {
		return new JsonSerialisingConverter<Configuration>(Configuration.class);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Function<Collection<String>, String> namesToString() {
		return Convert.toArray(new String[0]).andThen(new JsonSerialisingConverter<String[]>(String[].class));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Function<String, Collection<String>> toNames() {
		return new JsonDeserialisingConverter<>(String[].class).andThen(Arrays::asList);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Function<String, String> nameToString() {
		return new JsonSerialisingConverter<String>(String.class);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Function<String, Collection<IocState>> toIocStates() {
		return new IocsParametersConverter().andThen(new IocStateConverter());
	}
	
	private static <A, B> Function<A, B> withFunction(final Function<A, B> function) {
		return new Function<A, B>() {
			@Override
			public B apply(A value) throws ConversionException {
				return function.apply(value);
			}
		};
	}

	private static <T> Function<Collection<T>, Collection<T>> forEach(final Function<T, T> function) {
	    return value -> value.stream().map(function).collect(Collectors.toList());
	}
		
	private static final Function<Configuration, Configuration> INIT_CONFIG = uninitialized -> new Configuration(uninitialized);

	private static final Function<ComponentInfo, ComponentInfo> INIT_COMP = uninitialized -> new ComponentInfo(uninitialized);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Function<String, CustomBannerData> toBannerDescription() {
        return new JsonDeserialisingConverter<>(CustomBannerData.class);
    }
}
