package uk.ac.stfc.isis.ibex.banner;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.Channels;
import uk.ac.stfc.isis.ibex.instrument.InstrumentVariables;
import uk.ac.stfc.isis.ibex.instrument.channels.DoubleChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.EnumChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.LongChannel;

public class Observables extends InstrumentVariables {
	private Converter<Double, InMotionState> doubleToMotionState = new Converter<Double, InMotionState>() {
		@Override
		public InMotionState convert(Double value) throws ConversionException {
			if (value == null) {
				return InMotionState.UNKNOWN;
			}
			
			return value.equals(1.0) ? InMotionState.MOVING : InMotionState.STATIONARY;
		}
	};

	public final InitialiseOnSubscribeObservable<BumpStopState> bumpStop = reader(new EnumChannel<>(BumpStopState.class), "MOT:BUMP_STOP");
	public final InitialiseOnSubscribeObservable<InMotionState> inMotion = convert(reader(new DoubleChannel(), "CS:MOT:MOVING"), doubleToMotionState);	
	public final Writable<Long> stop = writable(new LongChannel(), "CS:MOT:STOP:ALL");
	
	public Observables(Channels channels) {
		super(channels);
	}		

}
