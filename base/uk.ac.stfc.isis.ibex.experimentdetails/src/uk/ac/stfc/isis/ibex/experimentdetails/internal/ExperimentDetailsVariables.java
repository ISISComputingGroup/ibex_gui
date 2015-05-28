package uk.ac.stfc.isis.ibex.experimentdetails.internal;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.writing.ConvertingWritable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.experimentdetails.Parameter;
import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;
import uk.ac.stfc.isis.ibex.instrument.Channels;
import uk.ac.stfc.isis.ibex.instrument.InstrumentVariables;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannelWithoutUnits;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;

public class ExperimentDetailsVariables extends InstrumentVariables {

	public final InitialiseOnSubscribeObservable<Collection<String>> availableSampleParameters 
		= convert(reader(new CompressedCharWaveformChannel(),"CS:BLOCKSERVER:SAMPLE_PARS"), new ParametersConverter());

	public final InitialiseOnSubscribeObservable<Collection<String>> availableBeamParameters 
		= convert(reader(new CompressedCharWaveformChannel(),"CS:BLOCKSERVER:BEAMLINE_PARS"), new ParametersConverter());

	public final InitialiseOnSubscribeObservable<Collection<Parameter>> sampleParameters 
		= autoInitialise(new ParametersObservable(this, availableSampleParameters));

	public final InitialiseOnSubscribeObservable<Collection<Parameter>> beamParameters 
		= autoInitialise(new ParametersObservable(this, availableBeamParameters));

	public final InitialiseOnSubscribeObservable<String> rbNumber = reader(new StringChannel(), "ED:RBNUMBER");
	public final Writable<String> rbNumberSetter = writable(new StringChannel(), "ED:RBNUMBER:SP");

	public final InitialiseOnSubscribeObservable<Collection<UserDetails>> userDetails 
		= convert(reader(new CompressedCharWaveformChannel(), "ED:USERNAME"), new UserDetailsConverter());
	
	public final Writable<UserDetails[]> userDetailsSetter
		= convert(writable(new CompressedCharWaveformChannel(), "ED:USERNAME:SP"), new UserDetailsSerialiser());

	public ExperimentDetailsVariables(Channels channels) {
		super(channels);
	}
	
	private <T> Writable<T> convert(Writable<String> destination, Converter<T, String> converter) {
		return new ConvertingWritable<>(destination, converter);
	}
	
	public InitialiseOnSubscribeObservable<String> parameterName(String address) {
		return reader(new StringChannel(), address + ".DESC");
	}
	
	public InitialiseOnSubscribeObservable<String> parameterUnits(String address) {
		return reader(new StringChannel(), address + ".EGU");
	}
	
	public InitialiseOnSubscribeObservable<String> parameterValue(String address) {
		return reader(new DefaultChannelWithoutUnits(), address);
	}

	public Writable<String> parameterValueSetter(String address) {
		return writable(new DefaultChannelWithoutUnits(), address + ":SP");
	}
}
