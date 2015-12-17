
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

package uk.ac.stfc.isis.ibex.instrument;

import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.observing.BaseCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ConvertingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;

public class InstrumentVariables extends Closer {

	private final Channels channels;

	public InstrumentVariables(Channels channels) {
		this.channels = channels;
	}
	
	protected <T> InitialiseOnSubscribeObservable<T> reader(ChannelType<T> type, String address) {
		return autoInitialise(registerForClose(channels.getReader(type, address)));
	}
	
	protected <T> Writable<T> writable(ChannelType<T> channelType, String address) {
		return registerForClose(channels.getWriter(channelType, address));
	}
	
	protected static <S, T> InitialiseOnSubscribeObservable<T> convert(ClosableCachingObservable<S> observable, Converter<S, T> converter) {
		return autoInitialise(new ConvertingObservable<>(observable, converter));
	}
	
	protected static <T> InitialiseOnSubscribeObservable<T> autoInitialise(BaseCachingObservable<T> observable) {
		return new InitialiseOnSubscribeObservable<>(observable);
	}
}
