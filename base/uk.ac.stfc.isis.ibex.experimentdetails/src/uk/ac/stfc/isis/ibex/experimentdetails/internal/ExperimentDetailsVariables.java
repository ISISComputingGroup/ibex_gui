
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

import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.ForwardingWritable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.experimentdetails.Parameter;
import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.InstrumentVariables;
import uk.ac.stfc.isis.ibex.instrument.channels.CharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannelWithoutUnits;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;

/**
 * Holds the Observables and Writables relating to experiment details.
 */
public class ExperimentDetailsVariables {

    private static final int INITIAL_STRING_CAPACITY = 50;

    private final ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);
    private final WritableFactory writeFactory = new WritableFactory(OnInstrumentSwitch.SWITCH);

    public final ForwardingObservable<Collection<String>> availableSampleParameters;
    public final ForwardingObservable<Collection<String>> availableBeamParameters;
    public final ForwardingObservable<Collection<Parameter>> sampleParameters;
    public final ForwardingObservable<Collection<Parameter>> beamParameters;

    public final ForwardingObservable<String> rbNumber;
    public final Writable<String> rbNumberSetter;

    public final ForwardingObservable<Collection<UserDetails>> userDetails;
    public final Writable<UserDetails[]> userDetailsSetter;

    public ExperimentDetailsVariables() {
        availableSampleParameters =
                InstrumentVariables.convert(obsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                addPrefix("CS:BLOCKSERVER:SAMPLE_PARS")),
                new ParametersConverter());
        availableBeamParameters =
                InstrumentVariables.convert(obsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                addPrefix("CS:BLOCKSERVER:BEAMLINE_PARS")),
                new ParametersConverter());
        sampleParameters =
                InstrumentVariables.autoInitialise(new ParametersObservable(this, availableSampleParameters));
        beamParameters = InstrumentVariables.autoInitialise(new ParametersObservable(this, availableBeamParameters));
        rbNumber = obsFactory.getSwitchableObservable(new StringChannel(), addPrefix("ED:RBNUMBER"));
        rbNumberSetter = writeFactory.getSwitchableWritable(new CharWaveformChannel(), addPrefix("ED:RBNUMBER:SP"));
        userDetails = InstrumentVariables.convert(
                obsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(), addPrefix("ED:USERNAME")),
                new UserDetailsConverter());
        userDetailsSetter = convert(
                writeFactory.getSwitchableWritable(new CompressedCharWaveformChannel(), addPrefix("ED:USERNAME:SP")),
                new UserDetailsSerialiser());
	}
	
	private <T> Writable<T> convert(Writable<String> destination, Converter<T, String> converter) {
        return new ForwardingWritable<>(destination, converter);
	}
	
	 /**
   * A helper method that returns the name of the parameter given its PV.
   * This based on the description field of the PV.
   * @param address The address of the PV that you want the name of.
   * @return An observable to the name of the parameter.
   */
	public ForwardingObservable<String> parameterName(String address) {
        return obsFactory.getSwitchableObservable(new StringChannel(), addPrefix(address + ".DESC"));
	}
	
	/**
	 * A helper method that returns an observable for the units of a parameter PV.
	 * @param address The address of the PV that you want the units of.
	 * @return An observable to the units of a PV.
	 */
	public ForwardingObservable<String> parameterUnits(String address) {
        return obsFactory.getSwitchableObservable(new StringChannel(), addPrefix(address + ".EGU"));
	}
	
	 /**
   * A helper method that returns an observable for a parameter PV.
   * @param address The address of the PV that you want to read from.
   * @return An observable to the PV.
   */
	public ForwardingObservable<String> parameterValue(String address) {
        return obsFactory.getSwitchableObservable(new DefaultChannelWithoutUnits(), addPrefix(address));
	}

  /**
   * A helper method that returns a writable for a parameter PV.
   * @param address The address of the PV that you want to write to.
   * @return A writable to the PV.
   */
	public Writable<String> parameterValueSetter(String address) {
        return writeFactory.getSwitchableWritable(new DefaultChannelWithoutUnits(), addPrefix(address + ":SP"));
	}

    private String addPrefix(String address) {
        StringBuilder sb = new StringBuilder(INITIAL_STRING_CAPACITY);
        sb.append(Instrument.getInstance().getPvPrefix());
        sb.append(address);
        return sb.toString();
    }
}
