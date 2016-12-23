
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

import uk.ac.stfc.isis.ibex.configserver.configuration.BannerItem;
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
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.ForwardingWritable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.EnumChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;
import uk.ac.stfc.isis.ibex.validators.BlockServerNameValidator;

/**
 * Holds all the Observables and Writables for the PVs associated with the
 * BlockServer.
 */
public class ConfigServerVariables extends Closer {
	private final BlockServerAddresses blockServerAddresses = new BlockServerAddresses();
	private final Converters converters;
	private ObservableFactory switchingObsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);
    private final WritableFactory switchingWriteFactory = new WritableFactory(OnInstrumentSwitch.SWITCH);
    private ObservableFactory closingObsFactory = new ObservableFactory(OnInstrumentSwitch.CLOSE);
    private final WritableFactory closingWriteFactory = new WritableFactory(OnInstrumentSwitch.CLOSE);    
	
    /** Provides the status of the block server. */
	public final ForwardingObservable<ServerStatus> serverStatus;
    /** Monitors the current configuration. */
	public final ForwardingObservable<Configuration> currentConfig;
    /** Provides an "empty" configuration. */
	public final ForwardingObservable<Configuration> blankConfig;
    /** Provides the configuration information for all the configurations. */
	public final ForwardingObservable<Collection<ConfigInfo>> configsInfo;
    /** Provides the component information for all the components. */
	public final ForwardingObservable<Collection<ConfigInfo>> componentsInfo;
    /** Rules from the block server for how the block should be named. */
	public final ForwardingObservable<BlockRules> blockRules;
    /** Rules from the block server for how the group should be named. */
    public final ForwardingObservable<BlockServerNameValidator> groupRules;
    /** Rules from the description should be formed. */
    public final ForwardingObservable<BlockServerNameValidator> configDescriptionRules;
    /** Provides the components on the instrument. */
	public final ForwardingObservable<Collection<Component>> components;
    /** Provides the IOCs on the instrument. */
	public final ForwardingObservable<Collection<EditableIoc>> iocs;
    /** Provides all the PVs on the instrument. */
	public final ForwardingObservable<Collection<PV>> pvs;
    /** Provides all the high interest PVs on the instrument. */
	public final ForwardingObservable<Collection<PV>> highInterestPVs;
    /** Provides all the medium interest PVs on the instrument. */
	public final ForwardingObservable<Collection<PV>> mediumInterestPVs;
    /** Provides all the interesting facility PVs on the instrument. */
    public final ForwardingObservable<Collection<PV>> facilityInterestPVs;
    /** Provides all the active PVs on the instrument. */
	public final ForwardingObservable<Collection<PV>> activePVs;
    /** Allows the current configuration to be saved. */
	public final Writable<Configuration> setCurrentConfiguration;
    /** Allows a configuration to be loaded. */
	public final Writable<String> loadConfiguration;
    /** Allows a configuration to be saved with a specific name. */
	public final Writable<Configuration> saveAsConfiguration;
    /** Allows a component to be saved with a specific name. */
	public final Writable<Configuration> saveAsComponent;
    /** Allows a configuration(s) to be deleted. */
	public final Writable<Collection<String>> deleteConfigurations;
    /** Allows a component(s) to be deleted. */
	public final Writable<Collection<String>> deleteComponents;
    /** Allows an IOC to be started. */
	public final Writable<Collection<String>> startIoc;
    /** Allows an IOC to be stopped. */
	public final Writable<Collection<String>> stopIoc;
    /** Allows an IOC to be restarted. */
	public final Writable<Collection<String>> restartIoc;
    /** Provides the states of the IOC (running, stopped etc.). */
	public final ForwardingObservable<Collection<IocState>> iocStates;
    /** Provides a list of IOCs that cannot be stopped or restarted. */
	public final ForwardingObservable<Collection<String>> protectedIocs;
    /** Provides the description for the spangle banner. */
	public final ForwardingObservable<Collection<BannerItem>> bannerDescription;
	
    /**
     * Set the configuration server variables from the block server using the
     * converters.
     * 
     * @param converters converters to use to convert values from block server
     *            to class instances for variables
     */
    public ConfigServerVariables(Converters converters) {
		this.converters = converters;
		
		serverStatus = convert(readCompressed(blockServerAddresses.serverStatus()), converters.toServerStatus());
		currentConfig = convert(readCompressed(blockServerAddresses.currentConfig()), converters.toConfig());
		blankConfig = convert(readCompressed(blockServerAddresses.blankConfig()), converters.toConfig());

		configsInfo = convert(readCompressed(blockServerAddresses.configs()), converters.toConfigsInfo());
		componentsInfo = convert(readCompressed(blockServerAddresses.components()), converters.toConfigsInfo());
		
        blockRules = convert(readCompressed(blockServerAddresses.blockRules()), converters.toBlockRules());
        groupRules = convert(readCompressed(blockServerAddresses.groupRules()), converters.toBlockServerTextValidor());
        configDescriptionRules = convert(readCompressed(blockServerAddresses.configDescritpionRules()),
                converters.toBlockServerTextValidor());
		
		components = convert(readCompressed(blockServerAddresses.components()), converters.toComponents());
		iocs = convert(readCompressed(blockServerAddresses.iocs()), converters.toIocs());
		pvs = convert(readCompressed(blockServerAddresses.pvs()), converters.toPVs());
		highInterestPVs = convert(readCompressed(blockServerAddresses.highInterestPVs()), converters.toPVs());
		mediumInterestPVs = convert(readCompressed(blockServerAddresses.mediumInterestPVs()), converters.toPVs());
        facilityInterestPVs = convert(readCompressed(blockServerAddresses.facilityInterestPVs()), converters.toPVs());
		activePVs = convert(readCompressed(blockServerAddresses.activePVs()), converters.toPVs());
		
		setCurrentConfiguration = convert(writeCompressed(blockServerAddresses.setCurrentConfig()), converters.configToString());
		loadConfiguration = convert(writeCompressed(blockServerAddresses.loadConfig()), converters.nameToString());
		saveAsConfiguration = convert(writeCompressed(blockServerAddresses.saveNewConfig()), converters.configToString());
		saveAsComponent = convert(writeCompressed(blockServerAddresses.saveComponent()), converters.configToString());
		
		deleteConfigurations = convert(writeCompressed(blockServerAddresses.deleteConfigs()), converters.namesToString());
		deleteComponents = convert(writeCompressed(blockServerAddresses.deleteComponents()), converters.namesToString());

		startIoc = convert(writeCompressed(blockServerAddresses.startIocs()), converters.namesToString());
		stopIoc = convert(writeCompressed(blockServerAddresses.stopIocs()), converters.namesToString());
		restartIoc = convert(writeCompressed(blockServerAddresses.restartIocs()), converters.namesToString());
				
		iocStates = convert(readCompressed(blockServerAddresses.iocs()), converters.toIocStates());
		protectedIocs = convert(readCompressed(blockServerAddresses.iocsNotToStop()), converters.toNames());	
        bannerDescription =
                convert(readCompressed(blockServerAddresses.bannerDescription()), converters.toBannerDescription());
	}

    /**
     * Provides a monitor on the specified configuration.
     * 
     * @param configName the configuration name
     * @return the corresponding observable
     */
	public ForwardingObservable<Configuration> config(String configName) {		
		return convert(readCompressedClosing(blockServerAddresses.config(getConfigPV(configName))), converters.toConfig());
	}

    /**
     * Provides a monitor on the specified component.
     * 
     * @param componentName the component name
     * @return the corresponding observable
     */
	public ForwardingObservable<Configuration> component(String componentName) {
		return convert(readCompressedClosing(blockServerAddresses.component(getComponentPV(componentName))), converters.toConfig());
	}

    /**
     * Provides a monitor on the specified IOC description.
     * 
     * @param iocName the IOC name
     * @return the corresponding observable
     */
	public ForwardingObservable<String> iocDescription(String iocName) {
        return closingObsFactory.getSwitchableObservable(new StringChannel(), addPrefix(iocDescriptionAddress(iocName)));
	}

    /**
     * Provides a writable for setting an IOC description.
     * 
     * @param iocName the IOC name
     * @return the corresponding writable
     */
	public Writable<String> iocDescriptionSetter(String iocName) {
        return closingWriteFactory.getSwitchableWritable(new StringChannel(), addPrefix(iocDescriptionAddress(iocName)));
	}
	
    /**
     * Provides a monitor on a specified block.
     * 
     * @param blockName the block name
     * @return the corresponding observable
     */
	public ForwardingObservable<String> blockValue(String blockName) {
        return closingObsFactory.getSwitchableObservable(new DefaultChannel(), addPrefix(blockServerAlias(blockName)));
	}
	
    /**
     * Provides a monitor on a specified block's description.
     * 
     * @param blockName the block name
     * @return the corresponding observable
     */
	public ForwardingObservable<String> blockDescription(String blockName) {
        return closingObsFactory.getSwitchableObservable(new StringChannel(),
                addPrefix(blockServerAddresses.blockDescription(blockServerAlias(blockName))));
	}

    /**
     * Returns an observable conveying the alarm state of a given block.
     * 
     * @param blockName the name of the block
     * @return the observable object
     */
    public ForwardingObservable<AlarmState> alarm(String blockName) {
        return closingObsFactory.getSwitchableObservable(new EnumChannel<>(AlarmState.class),
                addPrefix(blockServerAddresses.blockAlarm(blockServerAlias(blockName))));
    }

    /**
     * Gets the PV name for a block.
     * 
     * @param name the block
     * @return the PV name
     */
	public String blockServerAlias(String name) {
		return blockServerAddresses.blockAlias(name);
	}
	
    /**
     * Gets the PV name for an IOC's description.
     * 
     * @param iocName the IOC name
     * @return the PV name
     */
	private static String iocDescriptionAddress(String iocName) {
		return PVAddress.startWith("CS").append("PS").append(iocName).endWith("IOCDESC");
	}
	
    /**
     * Provides an observable that converts data from another observable.
     * 
     * @param <T> This is the type parameter
     * @param source the original observable
     * @param converter the converter
     * @return the new observable
     */
	private <T> ForwardingObservable<T> convert(
			ForwardingObservable<String> source,
			Converter<String, T> converter) {
		return new ForwardingObservable<>(new ConvertingObservable<>(source, converter));
	}
	
    /**
     * Provides a writable that converts data to be used by another writable.
     * 
     * @param <T> This is the type parameter
     * @param destination the destination writable
     * @param converter the converter
     * @return the new writable
     */
	private <T> Writable<T> convert(Writable<String> destination, Converter<T, String> converter) {
        return new ForwardingWritable<>(destination, converter);
	}

    /**
     * Provides an observable that reads compressed data and uncompresses it.
     * 
     * @param address the PV name
     * @return the new observable
     */
	private ForwardingObservable<String> readCompressed(String address) {
        return switchingObsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(), addPrefix(address));
	}
	
    /**
     * Provides a closing observable that reads compressed data and uncompresses
     * it.
     * 
     * @param address the PV name
     * @return the new observable
     */
	private ForwardingObservable<String> readCompressedClosing(String address) {
        return closingObsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(), addPrefix(address));
	}
	
    /**
     * Provides a writable that compresses data passed to it.
     * 
     * @param address the PV name
     * @return the new writable
     */
	private Writable<String> writeCompressed(String address) {
        return switchingWriteFactory.getSwitchableWritable(new CompressedCharWaveformChannel(), addPrefix(address));
	}
	
    /**
     * Constructs the name of the PV for the specified configuration.
     * 
     * @param configName the name
     * @return the PV name
     */
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
	
    /**
     * Constructs the name of the PV for the specified component.
     * 
     * @param componentName the name
     * @return the PV name
     */
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

    /**
     * Adds the PV prefix to an address.
     * 
     * @param address the unprefixed name
     * @return the full PV name
     */
    private String addPrefix(String address) {
        StringBuilder sb = new StringBuilder();
        sb.append(Instrument.getInstance().getPvPrefix());
        sb.append(address);
        return sb.toString();
    }
}
