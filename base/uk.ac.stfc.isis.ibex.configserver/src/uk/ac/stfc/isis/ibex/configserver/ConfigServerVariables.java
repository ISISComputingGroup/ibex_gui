
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

package uk.ac.stfc.isis.ibex.configserver;

import java.util.Collection;
import java.util.Locale;
import java.util.NoSuchElementException;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import uk.ac.stfc.isis.ibex.configserver.configuration.Component;
import uk.ac.stfc.isis.ibex.configserver.configuration.ConfigInfo;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.PV;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.configserver.internal.Converters;
import uk.ac.stfc.isis.ibex.configserver.pv.BlockServerAddresses;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.observing.ConvertingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.ConvertingWritable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.InstrumentVariables;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;

/**
 * Holds all the Observables and Writables for the PVs associated with the
 * BlockServer.
 */
public class ConfigServerVariables extends InstrumentVariables {

	private final BlockServerAddresses blockServerAddresses = new BlockServerAddresses();
	private final Converters converters;
    
	private ObservableFactory switchingObsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);
    private final WritableFactory switchingWriteFactory = new WritableFactory(OnInstrumentSwitch.SWITCH);
    private ObservableFactory closingObsFactory = new ObservableFactory(OnInstrumentSwitch.CLOSE);
    private final WritableFactory closingWriteFactory = new WritableFactory(OnInstrumentSwitch.CLOSE);    
	
	public final ForwardingObservable<ServerStatus> serverStatus;
	public final ForwardingObservable<Configuration> currentConfig;
	public final ForwardingObservable<Configuration> blankConfig;
	
	public final ForwardingObservable<Collection<ConfigInfo>> configsInfo;
	public final ForwardingObservable<Collection<ConfigInfo>> componentsInfo;

	public final ForwardingObservable<BlockRules> blockRules;
	
	public final ForwardingObservable<Collection<Component>> components;
	public final ForwardingObservable<Collection<EditableIoc>> iocs;
	public final ForwardingObservable<Collection<PV>> pvs;
	public final ForwardingObservable<Collection<PV>> highInterestPVs;
	public final ForwardingObservable<Collection<PV>> mediumInterestPVs;
    public final ForwardingObservable<Collection<PV>> facilityInterestPVs;
	public final ForwardingObservable<Collection<PV>> active_pvs;
	
	public final Writable<Configuration> setCurrentConfiguration;
	public final Writable<String> loadConfiguration;
	public final Writable<String> saveConfiguration;
	public final Writable<Configuration> saveAsConfiguration;
	public final Writable<Configuration> saveAsComponent;
	public final Writable<Collection<String>> deleteConfigurations;
	public final Writable<Collection<String>> deleteComponents;
	
	public final Writable<Collection<String>> startIoc;
	public final Writable<Collection<String>> stopIoc;
	public final Writable<Collection<String>> restartIoc;
	
	public final ForwardingObservable<Collection<IocState>> iocStates;
	public final ForwardingObservable<Collection<String>> protectedIocs;
	
    public ConfigServerVariables(Converters converters) {
		this.converters = converters;
		
		serverStatus = convert(readCompressed(blockServerAddresses.serverStatus()), converters.toServerStatus());
		currentConfig = convert(readCompressed(blockServerAddresses.currentConfig()), converters.toConfig());
		blankConfig = convert(readCompressed(blockServerAddresses.blankConfig()), converters.toConfig());

		configsInfo = convert(readCompressed(blockServerAddresses.configs()), converters.toConfigsInfo());
		componentsInfo = convert(readCompressed(blockServerAddresses.components()), converters.toConfigsInfo());
		
		blockRules = convert(readCompressed(blockServerAddresses.blockRules()), converters.toBlockRules());	
		
		components = convert(readCompressed(blockServerAddresses.components()), converters.toComponents());
		iocs = convert(readCompressed(blockServerAddresses.iocs()), converters.toIocs());
		pvs = convert(readCompressed(blockServerAddresses.pvs()), converters.toPVs());
		highInterestPVs = convert(readCompressed(blockServerAddresses.highInterestPVs()), converters.toPVs());
		mediumInterestPVs = convert(readCompressed(blockServerAddresses.mediumInterestPVs()), converters.toPVs());
        facilityInterestPVs = convert(readCompressed(blockServerAddresses.facilityInterestPVs()), converters.toPVs());
		active_pvs = convert(readCompressed(blockServerAddresses.activePVs()), converters.toPVs());
		
		setCurrentConfiguration = convert(writeCompressed(blockServerAddresses.setCurrentConfig()), converters.configToString());
		loadConfiguration = convert(writeCompressed(blockServerAddresses.loadConfig()), converters.nameToString());
		saveConfiguration = convert(writeCompressed(blockServerAddresses.saveConfig()), converters.nameToString());
		saveAsConfiguration = convert(writeCompressed(blockServerAddresses.saveNewConfig()), converters.configToString());
		saveAsComponent = convert(writeCompressed(blockServerAddresses.saveComponent()), converters.configToString());
		
		deleteConfigurations = convert(writeCompressed(blockServerAddresses.deleteConfigs()), converters.namesToString());
		deleteComponents = convert(writeCompressed(blockServerAddresses.deleteComponents()), converters.namesToString());

		startIoc = convert(writeCompressed(blockServerAddresses.startIocs()), converters.namesToString());
		stopIoc = convert(writeCompressed(blockServerAddresses.stopIocs()), converters.namesToString());
		restartIoc = convert(writeCompressed(blockServerAddresses.restartIocs()), converters.namesToString());
				
		iocStates = convert(readCompressed(blockServerAddresses.iocs()), converters.toIocStates());
		protectedIocs = convert(readCompressed(blockServerAddresses.iocsNotToStop()), converters.toNames());		
	}

	public ForwardingObservable<Configuration> config(String configName) {		
		return convert(readCompressedClosing(blockServerAddresses.config(getConfigPV(configName))), converters.toConfig());
	}

	public ForwardingObservable<Configuration> component(String componentName) {
		return convert(readCompressedClosing(blockServerAddresses.component(getComponentPV(componentName))), converters.toConfig());
	}

	public ForwardingObservable<String> iocDescription(String iocName) {
        return closingObsFactory.getSwitchableObservable(new StringChannel(), addPrefix(iocDescriptionAddress(iocName)));
	}

	public Writable<String> iocDescriptionSetter(String iocName) {
        return closingWriteFactory.getSwitchableWritable(new StringChannel(), addPrefix(iocDescriptionAddress(iocName)));
	}
	
	public ForwardingObservable<String> blockValue(String blockName) {
        return closingObsFactory.getSwitchableObservable(new DefaultChannel(), addPrefix(blockServerAlias(blockName)));
	}
	
	public ForwardingObservable<String> blockDescription(String blockName) {
        return closingObsFactory.getSwitchableObservable(new StringChannel(),
                addPrefix(blockServerAddresses.blockDescription(blockServerAlias(blockName))));
	}
	
	public String blockServerAlias(String name) {
		return blockServerAddresses.blockAlias(name);
	}
	
	private static String iocDescriptionAddress(String iocName) {
		return PVAddress.startWith("CS").append("PS").append(iocName).endWith("IOCDESC");
	}
	
	private <T> ForwardingObservable<T> convert(
			ForwardingObservable<String> source,
			Converter<String, T> converter) {
		return new ForwardingObservable<>(new ConvertingObservable<>(source, converter));
	}
	
	private <T> Writable<T> convert(Writable<String> destination, Converter<T, String> converter) {
		return new ConvertingWritable<>(destination, converter);
	}

	private ForwardingObservable<String> readCompressed(String address) {
        return switchingObsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(), addPrefix(address));
	}
	
	private ForwardingObservable<String> readCompressedClosing(String address) {
        return closingObsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(), addPrefix(address));
	}
	
	private Writable<String> writeCompressed(String address) {
        return switchingWriteFactory.getSwitchableWritable(new CompressedCharWaveformChannel(), addPrefix(address));
	}
	
	private String getConfigPV(final String configName) {
		try {
			return Iterables.find(configsInfo.getValue(), new Predicate<ConfigInfo>() {
				@Override
				public boolean apply(ConfigInfo info) {
					return info.name().equals(configName);
				}
			}).pv();
		} catch (NoSuchElementException e) {
			return configName.toUpperCase(Locale.ENGLISH);
		}
	}
	
	private String getComponentPV(final String componentName) {
		try {
			return Iterables.find(componentsInfo.getValue(), new Predicate<ConfigInfo>() {
				@Override
				public boolean apply(ConfigInfo info) {
					return info.name().equals(componentName);
				}
			}).pv();
		} catch (NoSuchElementException e) {
			return componentName.toUpperCase(Locale.ENGLISH);
		}
	}

    private String addPrefix(String address) {
        StringBuilder sb = new StringBuilder(50);
        sb.append(Instrument.getInstance().getPvPrefix());
        sb.append(address);
        return sb.toString();
    }
}
