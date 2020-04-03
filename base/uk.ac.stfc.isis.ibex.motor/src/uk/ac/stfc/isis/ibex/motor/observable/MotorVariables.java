
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

package uk.ac.stfc.isis.ibex.motor.observable;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.BooleanChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DoubleChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.EnumChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.ShortChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;
import uk.ac.stfc.isis.ibex.motor.MotorDirection;
import uk.ac.stfc.isis.ibex.motor.MotorEnable;

/**
 * Contains the variables for a motor.
 */
public class MotorVariables extends Closer {

	private static final Converter<Short, MotorDirection> TO_MOTOR_DIRECTION = new Converter<Short, MotorDirection>() {
		@Override
		public MotorDirection convert(Short value) throws ConversionException {
			if (value == null) {
				return MotorDirection.UNKNOWN;
			}
			
			return value > 0 ? MotorDirection.POSITIVE : MotorDirection.NEGATIVE;
		}
	};
	
	private static <T extends Number> Converter<T, Boolean> toBoolean() {
		return new Converter<T, Boolean>() {
			@Override
			public Boolean convert(T value) throws ConversionException {
				if (value == null) {
					return null;
				}
				
				return value.doubleValue() > 0.0 ? true : false;
			}
		};
	}
	
	private static enum EnergisedStatus {
		ON,
		OFF;
	}
	
	private static final Converter<EnergisedStatus, Boolean> ENERGISED_CONVERTER = new Converter<EnergisedStatus, Boolean>() {
		@Override
		public Boolean convert(EnergisedStatus value) {
			if (value == null) {
				return null;
			} else {
				return value == EnergisedStatus.ON;
			}
		}
	};
	
	private final PVAddress motorAddress;

    /** The name of the motor. */
	public final String motorName;

    /** The setpoint. */
	public final MotorSetPointVariables setpoint; 

    /** The description observable. */
	public final ForwardingObservable<String> description;

    /** The enable observable. */
	public final ForwardingObservable<MotorEnable> enable;
	
    /** The lower limit observable. */
	public final ForwardingObservable<Double> lowerLimit;

    /** The upper limit observable. */
	public final ForwardingObservable<Double> upperLimit;
	
    /** The offset observable. */
	public final ForwardingObservable<Double> offset;

    /** The error observable. */
	public final ForwardingObservable<Double> error;
	
    /** The direction observable. */
	public final ForwardingObservable<MotorDirection> direction;

    /** The moving observable. */
	public final ForwardingObservable<Boolean> moving;

    /** The "at home" observable. */
	public final ForwardingObservable<Boolean> atHome;
	
	/** The "using encoder" observable. */
	public final ForwardingObservable<Boolean> usingEncoder;
	
	/** The "energised" observable. */
	public final ForwardingObservable<Boolean> energised;

    /** The "at upper limit" observable. */
	public final ForwardingObservable<Boolean> atUpperLimitSwitch;

    /** The "at lower limit" observable. */
	public final ForwardingObservable<Boolean> atLowerLimitSwitch;

    /**
     * Constructor.
     * 
     * @param motorName the name of the motor
     * @param instrument the instrument
     */
	public MotorVariables(String motorName, Instrument instrument) {
		this.motorName = motorName;
        this.motorAddress = PVAddress.startWith("MOT").append(motorName);
        PVAddress fullAddress = PVAddress.startWith(instrument.getPvPrefix() + motorAddress);

        ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);
        WritableFactory writeFactory = new WritableFactory(OnInstrumentSwitch.SWITCH);
		
        description = obsFactory.getSwitchableObservable(new StringChannel(), fullAddress.endWithField("DESC"));
        enable = obsFactory.getSwitchableObservable(new EnumChannel<>(MotorEnable.class),
                fullAddress.toString() + "_able");
		
        lowerLimit = obsFactory.getSwitchableObservable(new DoubleChannel(), fullAddress.endWithField("DLLM"));
        upperLimit = obsFactory.getSwitchableObservable(new DoubleChannel(), fullAddress.endWithField("DHLM"));
        
        offset = obsFactory.getSwitchableObservable(new DoubleChannel(), fullAddress.endWithField("OFF"));
        error = obsFactory.getSwitchableObservable(new DoubleChannel(), fullAddress.endWithField("DIFF"));
        
        usingEncoder = obsFactory.getSwitchableObservable(new BooleanChannel(), fullAddress.endWithField("UEIP"));
        energised = InstrumentUtils.convert(
        		obsFactory.getSwitchableObservable(new EnumChannel<EnergisedStatus>(EnergisedStatus.class), fullAddress.toString() + "_ON_STATUS"), ENERGISED_CONVERTER);
        
        direction = InstrumentUtils.convert(
                obsFactory.getSwitchableObservable(new ShortChannel(), fullAddress.endWithField("TDIR")),
                TO_MOTOR_DIRECTION);
		
        moving = InstrumentUtils.convert(
                obsFactory.getSwitchableObservable(new ShortChannel(), fullAddress.endWithField("MOVN")), toBoolean());
        atHome = InstrumentUtils.convert(
                obsFactory.getSwitchableObservable(new ShortChannel(), fullAddress.endWithField("ATHM")), toBoolean());
        atUpperLimitSwitch = InstrumentUtils.convert(
                obsFactory.getSwitchableObservable(new ShortChannel(), fullAddress.endWithField("HLS")), toBoolean());
        atLowerLimitSwitch = InstrumentUtils.convert(
                obsFactory.getSwitchableObservable(new ShortChannel(), fullAddress.endWithField("LLS")), toBoolean());
        setpoint = new MotorSetPointVariables(fullAddress, obsFactory, writeFactory);
	}
	
    /**
     * Get the motor address.
     * 
     * @return the address
     */
	public String motorAddress() {
		return motorAddress.toString();
	}
}
