
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
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
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

		this.motorName = motorName;
        this.motorAddress = PVAddress.startWith("MOT").append(motorName);
        PVAddress fullAddress = PVAddress.startWith(instrument.getPvPrefix() + motorAddress);

        ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);
		
        description = obsFactory.getSwitchableObservable(new StringChannel(), fullAddress.endWithField("DESC"));
        enable = obsFactory.getSwitchableObservable(new EnumChannel<>(MotorEnable.class),
                fullAddress.toString() + "_able");
		
        lowerLimit = obsFactory.getSwitchableObservable(new DoubleChannel(), fullAddress.endWithField("DLLM"));
        upperLimit = obsFactory.getSwitchableObservable(new DoubleChannel(), fullAddress.endWithField("DHLM"));
		
        direction = convert(obsFactory.getSwitchableObservable(new ShortChannel(), fullAddress.endWithField("TDIR")),
                TO_MOTOR_DIRECTION);
		
        moving = convert(obsFactory.getSwitchableObservable(new ShortChannel(), fullAddress.endWithField("MOVN")),
                TO_BOOLEAN);
        atHome = convert(obsFactory.getSwitchableObservable(new ShortChannel(), fullAddress.endWithField("ATHM")),
                TO_BOOLEAN);
        atUpperLimitSwitch = convert(
                obsFactory.getSwitchableObservable(new ShortChannel(), fullAddress.endWithField("LLS")), TO_BOOLEAN);
        atLowerLimitSwitch = convert(
                obsFactory.getSwitchableObservable(new ShortChannel(), fullAddress.endWithField("HLS")), TO_BOOLEAN);
        setpoint = registerForClose(new MotorSetPointVariables(fullAddress, instrument.channels(), obsFactory));
		
		status = convert(obsFactory.getSwitchableObservable(new StringChannel(), motorAddress.toString() + "_STATUS"), CAPITALISE_FIRST_LETTER_ONLY);
	}
	
	public String motorAddress() {
		return motorAddress.toString();
	}
}
