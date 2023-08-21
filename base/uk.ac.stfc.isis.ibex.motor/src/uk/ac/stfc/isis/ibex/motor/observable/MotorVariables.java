
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

import java.util.function.Function;

import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
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

    private static final Function<Short, MotorDirection> TO_MOTOR_DIRECTION = value -> {
        if (value == null) {
            return MotorDirection.UNKNOWN;
        }

        return value > 0 ? MotorDirection.POSITIVE : MotorDirection.NEGATIVE;
    };
	
	private static final Function<Short, Boolean> TO_BOOLEAN = value -> {
	    if (value == null) {
	        return null;
	    }
	    return value > 0;
	};
	
	private enum EnergisedStatus {
		ON,
		OFF;
	}
	
	private static final Function<EnergisedStatus, Boolean> ENERGISED_CONVERTER = value -> {
	    if (value == null) {
	        return null;
	    } else {
	        return value == EnergisedStatus.ON;
	    }
	};
	
	private static final Function<Double, Boolean> GREATER_THAN_ZERO_CONVERTER = value -> {
	    if (value == null) {
	        return null;
	    }
	    return value > 0;
	};
	
	private final PVAddress motorAddress;

    /** The name of the motor. */
	public final String motorName;

    /** The setpoint observable. */
	public final ForwardingObservable<Double> setpoint; 

    /** The value of position (readback) observable. */
	public final ForwardingObservable<Double> value;
		
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
	
	/** The "done moving" observable. */
	public final ForwardingObservable<Boolean> doneMoving;

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

    /** The "within tolerance" observable. */
	public final ForwardingObservable<Boolean> withinTolerance;

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
		
        description = obsFactory.getSwitchableObservable(new StringChannel(), fullAddress.endWithField("DESC"));
        enable = obsFactory.getSwitchableObservable(new EnumChannel<>(MotorEnable.class),
                fullAddress.toString() + "_able");
		
        lowerLimit = obsFactory.getSwitchableObservable(new DoubleChannel(), fullAddress.endWithField("LLM"));
        upperLimit = obsFactory.getSwitchableObservable(new DoubleChannel(), fullAddress.endWithField("HLM"));
        
        offset = obsFactory.getSwitchableObservable(new DoubleChannel(), fullAddress.endWithField("OFF"));
        error = obsFactory.getSwitchableObservable(new DoubleChannel(), fullAddress.endWithField("DIFF"));
        
        usingEncoder =  InstrumentUtils.convert(
        		obsFactory.getSwitchableObservable(new DoubleChannel(), fullAddress.endWith("USING_ENCODER")), GREATER_THAN_ZERO_CONVERTER);
        energised = InstrumentUtils.convert(
        		obsFactory.getSwitchableObservable(new EnumChannel<EnergisedStatus>(EnergisedStatus.class), fullAddress.toString() + "_ON_STATUS"), ENERGISED_CONVERTER);
        
        direction = InstrumentUtils.convert(
                obsFactory.getSwitchableObservable(new ShortChannel(), fullAddress.endWithField("TDIR")),
                TO_MOTOR_DIRECTION);
		
        moving = InstrumentUtils.convert(
                obsFactory.getSwitchableObservable(new ShortChannel(), fullAddress.endWithField("MOVN")), TO_BOOLEAN);
        doneMoving = InstrumentUtils.convert(
                obsFactory.getSwitchableObservable(new ShortChannel(), fullAddress.endWithField("DMOV")), TO_BOOLEAN);
        atHome = InstrumentUtils.convert(
                obsFactory.getSwitchableObservable(new ShortChannel(), fullAddress.endWithField("ATHM")), TO_BOOLEAN);
        atUpperLimitSwitch = InstrumentUtils.convert(
                obsFactory.getSwitchableObservable(new ShortChannel(), fullAddress.endWithField("HLS")), TO_BOOLEAN);
        atLowerLimitSwitch = InstrumentUtils.convert(
                obsFactory.getSwitchableObservable(new ShortChannel(), fullAddress.endWithField("LLS")), TO_BOOLEAN);
        
        setpoint = obsFactory.getSwitchableObservable(new DoubleChannel(), fullAddress.endWith("SP"));
 
        value = obsFactory.getSwitchableObservable(new DoubleChannel(), fullAddress.endWithField("RBV"));
        
        withinTolerance = InstrumentUtils.convert(
		        obsFactory.getSwitchableObservable(new DoubleChannel(), fullAddress.endWith("IN_POSITION")), GREATER_THAN_ZERO_CONVERTER);
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
