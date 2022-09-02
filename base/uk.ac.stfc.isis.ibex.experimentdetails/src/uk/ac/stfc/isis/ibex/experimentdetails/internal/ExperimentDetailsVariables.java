
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

package uk.ac.stfc.isis.ibex.experimentdetails.internal;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.epics.conversion.json.JsonSerialisingConverter;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.experimentdetails.Parameter;
import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.BooleanChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.CharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannelWithoutUnits;
import uk.ac.stfc.isis.ibex.instrument.channels.LongChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;

/**
 * Holds the Observables and Writables relating to experiment details.
 */
public class ExperimentDetailsVariables {
    private final ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);
    private final WritableFactory writeFactory = new WritableFactory(OnInstrumentSwitch.SWITCH);
    private static final PVAddress DAE = PVAddress.startWith("DAE");

    /** The observable for the available sample parameters. */
    public final ForwardingObservable<Collection<String>> availableSampleParameters;
    /** The observable for the available beam parameters. */
    public final ForwardingObservable<Collection<String>> availableBeamParameters;
    
    /** The observable for the current settings of the sample parameters.*/
    public final ForwardingObservable<Collection<Parameter>> sampleParameters;
    /** The observable for the current settings of the beam parameters.*/
    public final ForwardingObservable<Collection<Parameter>> beamParameters;

    /** The observable for the current rb number. **/
    public final ForwardingObservable<String> rbNumber;
    /** The writable for the current rb number. **/
    public final Writable<String> rbNumberSetter;
    
    /** The observable for title display. **/
    public final ForwardingObservable<Boolean> displayTitle;
    /** The writable for title display. **/
    public final Writable<Long> displayTitleSetter;

    /** The observable for the current user details. **/
    public final ForwardingObservable<Collection<UserDetails>> userDetails;
    
    /** The writable for setting the current user details. **/
    public final Writable<UserDetails[]> userDetailsSetter;

    private JsonSerialisingConverter<UserDetails[]> userDetailsSerialiser = new JsonSerialisingConverter<UserDetails[]>(UserDetails[].class);
    
    public ExperimentDetailsVariables() {
        availableSampleParameters =
                InstrumentUtils.convert(obsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                        InstrumentUtils.addPrefix("CS:BLOCKSERVER:SAMPLE_PARS")), new ParametersConverter());
        availableBeamParameters =
                InstrumentUtils.convert(obsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                        InstrumentUtils.addPrefix("CS:BLOCKSERVER:BEAMLINE_PARS")), new ParametersConverter());
        sampleParameters =
                new ForwardingObservable<>(new ParametersObservable(this, availableSampleParameters));
        beamParameters = new ForwardingObservable<>(new ParametersObservable(this, availableBeamParameters));
        rbNumber = obsFactory.getSwitchableObservable(new StringChannel(), InstrumentUtils.addPrefix("ED:RBNUMBER"));
        rbNumberSetter = writeFactory.getSwitchableWritable(new CharWaveformChannel(),
                InstrumentUtils.addPrefix("ED:RBNUMBER:SP"));
        displayTitle = obsFactory.getSwitchableObservable(new BooleanChannel(),
                InstrumentUtils.addPrefix("DAE:TITLE:DISPLAY"));
        displayTitleSetter = writeFactory.getSwitchableWritable(new LongChannel(),
        		InstrumentUtils.addPrefix("DAE:TITLE:DISPLAY"));
        userDetails = InstrumentUtils.convert(
                obsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                        InstrumentUtils.addPrefix("ED:USERNAME")),
                new UserDetailsConverter());
        userDetailsSetter = InstrumentUtils.convert(
                writeFactory.getSwitchableWritable(new CompressedCharWaveformChannel(),
                        InstrumentUtils.addPrefix("ED:USERNAME:SP")), userDetailsSerialiser);
	}
	
	 /**
   * A helper method that returns the name of the parameter given its PV.
   * This based on the description field of the PV.
   * @param address The address of the PV that you want the name of.
   * @return An observable to the name of the parameter.
   */
	public ForwardingObservable<String> parameterName(String address) {
        return obsFactory.getSwitchableObservable(new StringChannel(), InstrumentUtils.addPrefix(address + ".DESC"));
	}
	
	/**
	 * A helper method that returns an observable for the units of a parameter PV.
	 * @param address The address of the PV that you want the units of.
	 * @return An observable to the units of a PV.
	 */
	public ForwardingObservable<String> parameterUnits(String address) {
        return obsFactory.getSwitchableObservable(new StringChannel(), InstrumentUtils.addPrefix(address + ".EGU"));
	}
	
	 /**
   * A helper method that returns an observable for a parameter PV.
   * @param address The address of the PV that you want to read from.
   * @return An observable to the PV.
   */
	public ForwardingObservable<String> parameterValue(String address) {
        return obsFactory.getSwitchableObservable(new DefaultChannelWithoutUnits(), InstrumentUtils.addPrefix(address));
	}

  /**
   * A helper method that returns a writable for a parameter PV.
   * @param address The address of the PV that you want to write to.
   * @return A writable to the PV.
   */
	public Writable<String> parameterValueSetter(String address) {
        return writeFactory.getSwitchableWritable(new DefaultChannelWithoutUnits(),
                InstrumentUtils.addPrefix(address + ":SP"));
	}
}
