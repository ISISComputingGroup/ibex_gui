
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

package uk.ac.stfc.isis.ibex.banner;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.configuration.BannerItem;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.DoubleChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.LongChannel;

/**
 * Holds the Observables and Writables for the Spangle Banner.
 */
public class Observables {
    private final ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);
    private final WritableFactory writeFactory = new WritableFactory(OnInstrumentSwitch.SWITCH);

	private Converter<Double, InMotionState> doubleToMotionState = new Converter<Double, InMotionState>() {
		@Override
		public InMotionState convert(Double value) throws ConversionException {
			if (value == null) {
				return InMotionState.UNKNOWN;
			}
			
			return value.equals(1.0) ? InMotionState.MOVING : InMotionState.STATIONARY;
		}
	};

    public final ForwardingObservable<Collection<BannerItem>> bannerDescription;
    public final ForwardingObservable<InMotionState> inMotion;
    public final Writable<Long> stop;
	
    /**
     * Constructs the object and points the class variables at the correct
     * observable objects.
     */
    public Observables() {
        bannerDescription = Configurations.getInstance().variables().bannerDescription;
        inMotion = InstrumentUtils.convert(obsFactory.getSwitchableObservable(new DoubleChannel(),
                InstrumentUtils.addPrefix("CS:MOT:MOVING")),
                doubleToMotionState);
        stop = writeFactory.getSwitchableWritable(new LongChannel(),
                InstrumentUtils.addPrefix("CS:MOT:STOP:ALL"));
	}		

}
