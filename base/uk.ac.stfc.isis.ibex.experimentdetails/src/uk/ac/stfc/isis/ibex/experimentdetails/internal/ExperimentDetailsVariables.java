
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
import uk.ac.stfc.isis.ibex.epics.writing.ConvertingWritable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.experimentdetails.Parameter;
import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;
import uk.ac.stfc.isis.ibex.instrument.Channels;
import uk.ac.stfc.isis.ibex.instrument.InstrumentVariables;
import uk.ac.stfc.isis.ibex.instrument.channels.CharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannelWithoutUnits;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;

public class ExperimentDetailsVariables extends InstrumentVariables {

	public final InitialiseOnSubscribeObservable<Collection<String>> availableSampleParameters 
		= convert(reader(new CompressedCharWaveformChannel(), "CS:BLOCKSERVER:SAMPLE_PARS"), new ParametersConverter());

	public final InitialiseOnSubscribeObservable<Collection<String>> availableBeamParameters 
		= convert(reader(new CompressedCharWaveformChannel(), "CS:BLOCKSERVER:BEAMLINE_PARS"), new ParametersConverter());

	public final InitialiseOnSubscribeObservable<Collection<Parameter>> sampleParameters 
		= autoInitialise(new ParametersObservable(this, availableSampleParameters));

	public final InitialiseOnSubscribeObservable<Collection<Parameter>> beamParameters 
		= autoInitialise(new ParametersObservable(this, availableBeamParameters));

	public final InitialiseOnSubscribeObservable<String> rbNumber = reader(new StringChannel(), "ED:RBNUMBER");
	public final Writable<String> rbNumberSetter = writable(new CharWaveformChannel(), "ED:RBNUMBER:SP");

	public final InitialiseOnSubscribeObservable<Collection<UserDetails>> userDetails 
		= convert(reader(new CompressedCharWaveformChannel(), "ED:USERNAME"), new UserDetailsConverter());
	
	public final Writable<UserDetails[]> userDetailsSetter
		= convert(writable(new CompressedCharWaveformChannel(), "ED:USERNAME:SP"), new UserDetailsSerialiser());

	public ExperimentDetailsVariables(Channels channels) {
		super(channels);
	}
	
	private <T> Writable<T> convert(Writable<String> destination, Converter<T, String> converter) {
		return new ConvertingWritable<>(destination, converter);
	}
	
	public InitialiseOnSubscribeObservable<String> parameterName(String address) {
		return reader(new StringChannel(), address + ".DESC");
	}
	
	public InitialiseOnSubscribeObservable<String> parameterUnits(String address) {
		return reader(new StringChannel(), address + ".EGU");
	}
	
	public InitialiseOnSubscribeObservable<String> parameterValue(String address) {
		return reader(new DefaultChannelWithoutUnits(), address);
	}

	public Writable<String> parameterValueSetter(String address) {
		return writable(new DefaultChannelWithoutUnits(), address + ":SP");
	}
}
