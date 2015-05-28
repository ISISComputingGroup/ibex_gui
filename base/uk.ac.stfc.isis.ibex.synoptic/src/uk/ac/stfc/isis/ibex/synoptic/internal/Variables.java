package uk.ac.stfc.isis.ibex.synoptic.internal;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.epics.conversion.Convert;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.writing.ConvertingWritable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.Channels;
import uk.ac.stfc.isis.ibex.instrument.InstrumentVariables;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannelWithoutUnits;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;
import uk.ac.stfc.isis.ibex.json.JsonDeserialisingConverter;
import uk.ac.stfc.isis.ibex.json.JsonSerialisingConverter;
import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.InstrumentDescription;

public class Variables extends InstrumentVariables {
	
	private static final String SYNOPTIC_ADDRESS = "CS:BLOCKSERVER:SYNOPTICS:";
	private static final String GET_SYNOPTIC = ":GET";	
	
	public final InitialiseOnSubscribeObservable<InstrumentDescription> default_synoptic 
		= convert(readCompressed(SYNOPTIC_ADDRESS + "GET_DEFAULT"), new InstrumentDescriptionParser());
	
	public final Writable<String> setSynoptic = writeCompressed(SYNOPTIC_ADDRESS + "SET_DETAILS");
	
	public final Writable<Collection<String>> deleteSynoptics = convert(writeCompressed(SYNOPTIC_ADDRESS + "DELETE"), namesToString());
	
	public final InitialiseOnSubscribeObservable<Collection<SynopticInfo>> available 
	= convert(readCompressed(SYNOPTIC_ADDRESS + "NAMES"), toSynopticInfo());
	
	public Converter<String, Collection<SynopticInfo>> toSynopticInfo() {
		return new JsonDeserialisingConverter<>(SynopticInfo[].class).apply(Convert.<SynopticInfo>toCollection());
	}	
	
	public InitialiseOnSubscribeObservable<InstrumentDescription> getSynoptic(String synopticPV) {		
		return convert(readCompressed(getFullPV(synopticPV)), new InstrumentDescriptionParser());
	}
		
	private String getFullPV(String synopticPV) {
		return SYNOPTIC_ADDRESS + synopticPV + GET_SYNOPTIC;
	}	
	
	public Variables(Channels channels) {
		super(channels);
	}
	
	private Converter<Collection<String>, String> namesToString() {
		return Convert.toArray(new String[0]).apply(new JsonSerialisingConverter<String[]>(String[].class));
	}
	
	private Writable<String> writeCompressed(String address) {
		return writable(new CompressedCharWaveformChannel(), address);
	}
	
	private <T> Writable<T> convert(Writable<String> destination, Converter<T, String> converter) {
		return new ConvertingWritable<>(destination, converter);
	}	
	
	private InitialiseOnSubscribeObservable<String> readCompressed(String address) {
		return reader(new CompressedCharWaveformChannel(), address);
	}	
	
	// The following readers/writers are for PVs on the synoptic
	
	public InitialiseOnSubscribeObservable<String> defaultReader(String address) {
		return reader(new DefaultChannel(), address);
	}

	public InitialiseOnSubscribeObservable<String> defaultReaderWithoutUnits(String address) {
		return reader(new DefaultChannelWithoutUnits(), address);
	}
	
	public Writable<String> defaultWritable(String address) {
		return writable(new StringChannel(), address);
	}
	
}
