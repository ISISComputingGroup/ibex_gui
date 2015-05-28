package uk.ac.stfc.isis.ibex.motor.observable;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.InstrumentVariables;
import uk.ac.stfc.isis.ibex.instrument.channels.DoubleChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.EnumChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.ShortChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;
import uk.ac.stfc.isis.ibex.motor.MotorDirection;
import uk.ac.stfc.isis.ibex.motor.MotorEnable;

public class MotorVariables extends InstrumentVariables {

	private static final Converter<Short, MotorDirection> TO_MOTOR_DIRECTION = new Converter<Short, MotorDirection>() {
		@Override
		public MotorDirection convert(Short value) throws ConversionException {
			if (value == null) {
				return MotorDirection.UNKNOWN;
			}
			
			return value > 0 ? MotorDirection.POSITIVE : MotorDirection.NEGATIVE;
		}
	};

	private static final Converter<Short, Boolean> TO_BOOLEAN = new Converter<Short, Boolean>() {
		@Override
		public Boolean convert(Short value) throws ConversionException {
			if (value == null) {
				return null;
			}
			
			return value > 0 ? true : false;
		}
	};

	private static final Converter<String, String> CAPITALISE_FIRST_LETTER_ONLY = new Converter<String, String>() {
		@Override
		public String convert(String value) throws ConversionException {
			if (value == null || value.length() == 0) {
				return value;
			}
			
			return Character.toUpperCase(value.charAt(0)) + value.substring(1);
		}
	};
	
	private final Instrument instrument;;
	private final PVAddress motorAddress;

	public final String motorName;
	public final MotorSetPointVariables setpoint; 
	public final InitialiseOnSubscribeObservable<String> description;
	public final InitialiseOnSubscribeObservable<MotorEnable> enable;
	
	public final InitialiseOnSubscribeObservable<Double> lowerLimit;
	public final InitialiseOnSubscribeObservable<Double> upperLimit;
	
	public final InitialiseOnSubscribeObservable<MotorDirection> direction;
	public final InitialiseOnSubscribeObservable<Boolean> moving;
	public final InitialiseOnSubscribeObservable<Boolean> atHome;
	public final InitialiseOnSubscribeObservable<Boolean> atUpperLimitSwitch;
	public final InitialiseOnSubscribeObservable<Boolean> atLowerLimitSwitch;
	
	public final InitialiseOnSubscribeObservable<String> status;

	public MotorVariables(String motorName, Instrument instrument) {
		super(instrument.channels());

		this.instrument = instrument;
		this.motorName = motorName;
		this.motorAddress = PVAddress.startWith("MOT").append(motorName);
		
		description = reader(new StringChannel(), motorAddress.endWithField("DESC"));
		enable = reader(new EnumChannel<>(MotorEnable.class), motorAddress.toString() + "_able");
		
		lowerLimit = reader(new DoubleChannel(), motorAddress.endWithField("DLLM"));
		upperLimit = reader(new DoubleChannel(), motorAddress.endWithField("DHLM"));
		
		direction = convert(reader(new ShortChannel(), motorAddress.endWithField("TDIR")), TO_MOTOR_DIRECTION);
		
		moving = convert(reader(new ShortChannel(), motorAddress.endWithField("MOVN")), TO_BOOLEAN);
		atHome = convert(reader(new ShortChannel(), motorAddress.endWithField("ATHM")), TO_BOOLEAN);
		atUpperLimitSwitch = convert(reader(new ShortChannel(), motorAddress.endWithField("LLS")), TO_BOOLEAN);
		atLowerLimitSwitch = convert(reader(new ShortChannel(), motorAddress.endWithField("HLS")), TO_BOOLEAN);
		setpoint = registerForClose(new MotorSetPointVariables(motorAddress, instrument.channels()));
		
		status = convert(reader(new StringChannel(), motorAddress.toString() + "_STATUS"), CAPITALISE_FIRST_LETTER_ONLY);
	}
	
	public String motorAddress() {
		return motorAddress.toString();
	}
}
