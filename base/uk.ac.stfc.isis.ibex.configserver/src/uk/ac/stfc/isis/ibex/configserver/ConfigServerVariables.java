package uk.ac.stfc.isis.ibex.configserver;

import java.util.Collection;
import java.util.Locale;
import java.util.NoSuchElementException;

import uk.ac.stfc.isis.ibex.configserver.configuration.Component;
import uk.ac.stfc.isis.ibex.configserver.configuration.ConfigInfo;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.PV;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.configserver.internal.Converters;
import uk.ac.stfc.isis.ibex.configserver.pv.BlockServerAddresses;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.observing.ConvertingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;
import uk.ac.stfc.isis.ibex.epics.writing.ConvertingWritable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.Channels;
import uk.ac.stfc.isis.ibex.instrument.InstrumentVariables;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class ConfigServerVariables extends InstrumentVariables {

	private final BlockServerAddresses addresses = new BlockServerAddresses();
	private final Converters converters;
	
	public final InitialiseOnSubscribeObservable<ServerStatus> serverStatus;
	public final InitialiseOnSubscribeObservable<Configuration> currentConfig;
	public final InitialiseOnSubscribeObservable<Configuration> blankConfig;
	
	public final InitialiseOnSubscribeObservable<Collection<ConfigInfo>> configsInfo;
	public final InitialiseOnSubscribeObservable<Collection<ConfigInfo>> componentsInfo;
	
	public final InitialiseOnSubscribeObservable<Collection<Component>> components;
	public final InitialiseOnSubscribeObservable<Collection<EditableIoc>> iocs;
	public final InitialiseOnSubscribeObservable<Collection<PV>> pvs;
	public final InitialiseOnSubscribeObservable<Collection<PV>> highInterestPVs;
	public final InitialiseOnSubscribeObservable<Collection<PV>> mediumInterestPVs;
	public final InitialiseOnSubscribeObservable<Collection<PV>> active_pvs;
	
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
	
	public final InitialiseOnSubscribeObservable<Collection<IocState>> iocStates;
	public final InitialiseOnSubscribeObservable<Collection<String>> protectedIocs;
	
	public ConfigServerVariables(Channels channels, Converters converters) {
		super(channels);
		this.converters = converters;
		
		serverStatus = convert(readCompressed(addresses.serverStatus()), converters.toServerStatus());
		currentConfig = convert(readCompressed(addresses.currentConfig()), converters.toConfig());
		blankConfig = convert(readCompressed(addresses.blankConfig()), converters.toConfig());

		configsInfo = convert(readCompressed(addresses.configs()), converters.toConfigsInfo());
		componentsInfo = convert(readCompressed(addresses.components()), converters.toConfigsInfo());
		
		components = convert(readCompressed(addresses.components()), converters.toComponents());
		iocs = convert(readCompressed(addresses.iocs()), converters.toIocs());
		pvs = convert(readCompressed(addresses.pvs()), converters.toPVs());
		highInterestPVs = convert(readCompressed(addresses.highInterestPVs()), converters.toPVs());
		mediumInterestPVs = convert(readCompressed(addresses.mediumInterestPVs()), converters.toPVs());
		active_pvs = convert(readCompressed(addresses.activePVs()), converters.toPVs());
		
		setCurrentConfiguration = convert(writeCompressed(addresses.setCurrentConfig()), converters.configToString());
		loadConfiguration = convert(writeCompressed(addresses.loadConfig()), converters.nameToString());
		saveConfiguration = convert(writeCompressed(addresses.saveConfig()), converters.nameToString());
		saveAsConfiguration = convert(writeCompressed(addresses.saveNewConfig()), converters.configToString());
		saveAsComponent = convert(writeCompressed(addresses.saveComponent()), converters.configToString());
		
		deleteConfigurations = convert(writeCompressed(addresses.deleteConfigs()), converters.namesToString());
		deleteComponents = convert(writeCompressed(addresses.deleteComponents()), converters.namesToString());

		startIoc = convert(writeCompressed(addresses.startIocs()), converters.namesToString());
		stopIoc = convert(writeCompressed(addresses.stopIocs()), converters.namesToString());
		restartIoc = convert(writeCompressed(addresses.restartIocs()), converters.namesToString());
				
		iocStates = convert(readCompressed(addresses.iocs()), converters.toIocStates());
		protectedIocs = convert(readCompressed(addresses.iocsNotToStop()), converters.toNames());		
	}

	public InitialiseOnSubscribeObservable<Configuration> config(String configName) {		
		return convert(readCompressed(addresses.config(getConfigPV(configName))), converters.toConfig());
	}

	public InitialiseOnSubscribeObservable<Configuration> component(String componentName) {
		return convert(readCompressed(addresses.component(componentName)), converters.toConfig());
	}

	public InitialiseOnSubscribeObservable<String> iocDescription(String iocName) {
		return reader(new StringChannel(), iocDescriptionAddress(iocName));
	}

	public Writable<String> iocDescriptionSetter(String iocName) {
		return writable(new StringChannel(), iocDescriptionAddress(iocName));
	}
	
	public InitialiseOnSubscribeObservable<String> blockValue(String blockName) {
		return reader(new DefaultChannel(), blockServerAlias(blockName));
	}
	
	public InitialiseOnSubscribeObservable<String> blockDescription(String blockName) {
		return reader(new StringChannel(), addresses.blockDescription(blockServerAlias(blockName)));
	}
	
	public String blockServerAlias(String name) {
		return addresses.blockAlias(name);
	}
	
	private static String iocDescriptionAddress(String iocName) {
		return PVAddress.startWith("CS").append("PS").append(iocName).endWith("IOCDESC");
	}
	
	private <T> InitialiseOnSubscribeObservable<T> convert(
			InitialiseOnSubscribeObservable<String> source,
			Converter<String, T> converter) {
		return new InitialiseOnSubscribeObservable<>(new ConvertingObservable<>(source, converter));
	}
	
	private <T> Writable<T> convert(Writable<String> destination, Converter<T, String> converter) {
		return new ConvertingWritable<>(destination, converter);
	}

	private InitialiseOnSubscribeObservable<String> readCompressed(String address) {
		return reader(new CompressedCharWaveformChannel(), address);
	}
	
	private Writable<String> writeCompressed(String address) {
		return writable(new CompressedCharWaveformChannel(), address);
	}
	
	private String getConfigPV(final String configName) {
		try {
			return Iterables.find(configsInfo.value(), new Predicate<ConfigInfo>() {
				@Override
				public boolean apply(ConfigInfo info) {
					return info.name().equals(configName);
				}
			}).pv();
		} catch (NoSuchElementException e) {
			return configName.toUpperCase(Locale.ENGLISH);
		}
	}
}
