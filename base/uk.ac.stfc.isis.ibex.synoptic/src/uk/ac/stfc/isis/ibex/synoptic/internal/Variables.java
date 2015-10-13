
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

package uk.ac.stfc.isis.ibex.synoptic.internal;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.epics.conversion.Convert;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.writing.ConvertingWritable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.Channels;
import uk.ac.stfc.isis.ibex.instrument.InstrumentVariables;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannelWithoutUnits;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;
import uk.ac.stfc.isis.ibex.instrument.pv.PVType;
import uk.ac.stfc.isis.ibex.json.JsonDeserialisingConverter;
import uk.ac.stfc.isis.ibex.json.JsonSerialisingConverter;
import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;

/**
 * Holds all the PVs relating to the synoptic information.
 *
 */
public class Variables extends InstrumentVariables {
	
	private static final String SYNOPTIC_ADDRESS = "CS:BLOCKSERVER:SYNOPTICS:";
	private static final String GET_SYNOPTIC = ":GET";
	
	/**
	 * The name associated with the "blank" synoptic from the BlockServer.
	 */
	public static final String NONE_SYNOPTIC_NAME = "-- NONE --";
	
	public final InitialiseOnSubscribeObservable<SynopticDescription> default_synoptic 
		= convert(readCompressed(SYNOPTIC_ADDRESS + "GET_DEFAULT"), new InstrumentDescriptionParser());
	
	public final Writable<String> setSynoptic = writeCompressed(SYNOPTIC_ADDRESS + "SET_DETAILS");
	
	public final Writable<Collection<String>> deleteSynoptics = convert(writeCompressed(SYNOPTIC_ADDRESS + "DELETE"), namesToString());
	
	public final InitialiseOnSubscribeObservable<Collection<SynopticInfo>> available 
	= convert(readCompressed(SYNOPTIC_ADDRESS + "NAMES"), toSynopticInfo());
	
	public Converter<String, Collection<SynopticInfo>> toSynopticInfo() {
		return new JsonDeserialisingConverter<>(SynopticInfo[].class).apply(Convert.<SynopticInfo>toCollection());
	}	
	
	public InitialiseOnSubscribeObservable<SynopticDescription> getSynopticDescription(String synopticPV) {		
		return convert(readCompressed(getFullPV(synopticPV)), new InstrumentDescriptionParser());
	}
	
	/**
	 * An observable for the schema for the synoptics.
	 */
	public InitialiseOnSubscribeObservable<String> synopticSchema	
		= readCompressed(SYNOPTIC_ADDRESS + "SCHEMA");
		
	private String getFullPV(String synopticPV) {
		return SYNOPTIC_ADDRESS + synopticPV + GET_SYNOPTIC;
	}	
	
	public Variables(Channels channels) {
		super(channels);
	}
	
	private Converter<Collection<String>, String> namesToString() {
		return Convert.toArray(new String[0]).apply(new JsonSerialisingConverter<String[]>(String[].class));
	}
	
	private Writable<String> writeCompressed(String address) {
		return writable(new CompressedCharWaveformChannel(), address);
	}
	
	private <T> Writable<T> convert(Writable<String> destination, Converter<T, String> converter) {
		return new ConvertingWritable<>(destination, converter);
	}	
	
	private InitialiseOnSubscribeObservable<String> readCompressed(String address) {
		return reader(new CompressedCharWaveformChannel(), address);
	}	
	
	// The following readers/writers are for PVs on the synoptic
	
	public InitialiseOnSubscribeObservable<String> defaultReader(String address) {
		return reader(new DefaultChannel(), address);
	}
	
	public InitialiseOnSubscribeObservable<String> defaultReader(String address, PVType type) {
		return reader(new DefaultChannel(), address, type);
	}

	public InitialiseOnSubscribeObservable<String> defaultReaderWithoutUnits(String address) {
		return reader(new DefaultChannelWithoutUnits(), address);
	}
	
	public InitialiseOnSubscribeObservable<String> defaultReaderWithoutUnits(String address, PVType type) {
		return reader(new DefaultChannelWithoutUnits(), address, type);
	}
	
	public Writable<String> defaultWritable(String address) {
		return writable(new StringChannel(), address);
	}
	
	public Writable<String> defaultWritable(String address, PVType type) {
		return writable(new StringChannel(), address, type);
	}
	
}
