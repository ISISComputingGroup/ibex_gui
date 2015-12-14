
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
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.writing.ConvertingWritable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.experimentdetails.Parameter;
import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;
import uk.ac.stfc.isis.ibex.instrument.Channels;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.InstrumentVariables;
import uk.ac.stfc.isis.ibex.instrument.channels.CharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannelWithoutUnits;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;

/**
 * Holds the Observables and Writables relating to experiment details.
 */
public class ExperimentDetailsVariables extends InstrumentVariables {
    private final ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);

    public final InitialiseOnSubscribeObservable<Collection<String>> availableSampleParameters;
    public final InitialiseOnSubscribeObservable<Collection<String>> availableBeamParameters;
    public final InitialiseOnSubscribeObservable<Collection<Parameter>> sampleParameters;
    public final InitialiseOnSubscribeObservable<Collection<Parameter>> beamParameters;

    public final InitialiseOnSubscribeObservable<String> rbNumber;
    public final Writable<String> rbNumberSetter;

    public final InitialiseOnSubscribeObservable<Collection<UserDetails>> userDetails;
    public final Writable<UserDetails[]> userDetailsSetter;

	public ExperimentDetailsVariables(Channels channels) {
		super(channels);

        availableSampleParameters = convert(obsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                addPrefix("CS:BLOCKSERVER:SAMPLE_PARS")),
                new ParametersConverter());
        availableBeamParameters = convert(obsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                addPrefix("CS:BLOCKSERVER:BEAMLINE_PARS")),
                new ParametersConverter());
        sampleParameters = autoInitialise(new ParametersObservable(this, availableSampleParameters));
        beamParameters = autoInitialise(new ParametersObservable(this, availableBeamParameters));
        rbNumber = obsFactory.getSwitchableObservable(new StringChannel(), addPrefix("ED:RBNUMBER"));
        rbNumberSetter = writable(new CharWaveformChannel(), addPrefix("ED:RBNUMBER:SP"));
        userDetails = convert(
                obsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(), addPrefix("ED:USERNAME")),
                new UserDetailsConverter());
        userDetailsSetter = convert(writable(new CompressedCharWaveformChannel(), addPrefix("ED:USERNAME:SP")),
                new UserDetailsSerialiser());
	}
	
	private <T> Writable<T> convert(Writable<String> destination, Converter<T, String> converter) {
		return new ConvertingWritable<>(destination, converter);
	}
	
	public InitialiseOnSubscribeObservable<String> parameterName(String address) {
        return obsFactory.getSwitchableObservable(new StringChannel(), addPrefix(address + ".DESC"));
	}
	
	public InitialiseOnSubscribeObservable<String> parameterUnits(String address) {
        return obsFactory.getSwitchableObservable(new StringChannel(), addPrefix(address + ".EGU"));
	}
	
	public InitialiseOnSubscribeObservable<String> parameterValue(String address) {
        return obsFactory.getSwitchableObservable(new DefaultChannelWithoutUnits(), addPrefix(address));
	}

	public Writable<String> parameterValueSetter(String address) {
		return writable(new DefaultChannelWithoutUnits(), address + ":SP");
	}

    private String addPrefix(String address) {
        StringBuilder sb = new StringBuilder(50);
        sb.append(Instrument.getInstance().getPvPrefix());
        sb.append(address);
        return sb.toString();
    }
}
