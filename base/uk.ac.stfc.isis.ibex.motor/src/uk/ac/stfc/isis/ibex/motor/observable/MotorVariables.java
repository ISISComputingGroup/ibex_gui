
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
import uk.ac.stfc.isis.ibex.instrument.channels.DoubleChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.EnumChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.ShortChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;
import uk.ac.stfc.isis.ibex.motor.MotorDirection;
import uk.ac.stfc.isis.ibex.motor.MotorEnable;

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
	
	private final PVAddress motorAddress;

	public final String motorName;
	public final MotorSetPointVariables setpoint; 
	public final ForwardingObservable<String> description;
	public final ForwardingObservable<MotorEnable> enable;
	
	public final ForwardingObservable<Double> lowerLimit;
	public final ForwardingObservable<Double> upperLimit;
	
	public final ForwardingObservable<MotorDirection> direction;
	public final ForwardingObservable<Boolean> moving;
	public final ForwardingObservable<Boolean> atHome;
	public final ForwardingObservable<Boolean> atUpperLimitSwitch;
	public final ForwardingObservable<Boolean> atLowerLimitSwitch;
	
	public final ForwardingObservable<String> status;

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
		
        direction = InstrumentUtils.convert(
                obsFactory.getSwitchableObservable(new ShortChannel(), fullAddress.endWithField("TDIR")),
                TO_MOTOR_DIRECTION);
		
        moving = InstrumentUtils.convert(
                obsFactory.getSwitchableObservable(new ShortChannel(), fullAddress.endWithField("MOVN")),
                TO_BOOLEAN);
        atHome = InstrumentUtils.convert(
                obsFactory.getSwitchableObservable(new ShortChannel(), fullAddress.endWithField("ATHM")),
                TO_BOOLEAN);
        atUpperLimitSwitch = InstrumentUtils.convert(
                obsFactory.getSwitchableObservable(new ShortChannel(), fullAddress.endWithField("LLS")), TO_BOOLEAN);
        atLowerLimitSwitch = InstrumentUtils.convert(
                obsFactory.getSwitchableObservable(new ShortChannel(), fullAddress.endWithField("HLS")), TO_BOOLEAN);
        setpoint = new MotorSetPointVariables(fullAddress, obsFactory, writeFactory);
		
        status = InstrumentUtils.convert(
                obsFactory.getSwitchableObservable(new StringChannel(), motorAddress.toString() + "_STATUS"),
                CAPITALISE_FIRST_LETTER_ONLY);
	}
	
	public String motorAddress() {
		return motorAddress.toString();
	}
}
