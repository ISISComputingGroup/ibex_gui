
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
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
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
	
    public MotorSetPointVariables(PVAddress motorAddress, ObservableFactory obsFactory, WritableFactory writeFactory) {
        value = obsFactory.getSwitchableObservable(new DoubleChannel(), motorAddress.endWithField("RBV"));
		
		String setPointAddress = motorAddress.endWith("SP");
        setPoint = obsFactory.getSwitchableObservable(new DoubleChannel(), setPointAddress);

        setPointSetter = writeFactory.getSwitchableWritable(new DoubleChannel(), setPointAddress);
		
		String homeAddress = motorAddress.endWithField("HOMR");
        canHome = convert(obsFactory.getSwitchableObservable(new DoubleChannel(), homeAddress), TO_BOOLEAN);

        homeSetter = writeFactory.getSwitchableWritable(new DoubleChannel(), homeAddress);
	}
}
