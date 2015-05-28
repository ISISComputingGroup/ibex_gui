package uk.ac.stfc.isis.ibex.motor.observable;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.Channels;
import uk.ac.stfc.isis.ibex.instrument.InstrumentVariables;
import uk.ac.stfc.isis.ibex.instrument.channels.DoubleChannel;

public class MotorSetPointVariables extends InstrumentVariables {

	private static final Converter<Double, Boolean> TO_BOOLEAN = new Converter<Double, Boolean>() {
		@Override
		public Boolean convert(Double value) throws ConversionException {
			return value != null && value == 0.0;
		}
	};
	
	public final InitialiseOnSubscribeObservable<Double> value;
	public final InitialiseOnSubscribeObservable<Double> setPoint;
	public final Writable<Double> setPointSetter;
	public final InitialiseOnSubscribeObservable<Boolean> canHome;
	public final Writable<Double> homeSetter;
	
	public MotorSetPointVariables(PVAddress motorAddress, Channels channels) {
		super(channels);
		
		value = reader(new DoubleChannel(), motorAddress.endWithField("RBV"));
		
		String setPointAddress = motorAddress.endWith("SP");
		setPoint = reader(new DoubleChannel(), setPointAddress);
		setPointSetter = writable(new DoubleChannel(), setPointAddress);
		
		String homeAddress = motorAddress.endWithField("HOMR");
		canHome = convert(reader(new DoubleChannel(), homeAddress), TO_BOOLEAN);
		homeSetter = writable(new DoubleChannel(), homeAddress);
	}
}
