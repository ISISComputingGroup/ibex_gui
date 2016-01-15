
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

package uk.ac.stfc.isis.ibex.configserver.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import uk.ac.stfc.isis.ibex.configserver.configuration.PV;
import uk.ac.stfc.isis.ibex.configserver.pv.ChannelTypeLookup;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.TransformingObservable;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;

public class ChannelResolver extends TransformingObservable<Collection<PV>, ChannelResolver> {
	
	private Map<String, PV> lookup = new HashMap<>();
	
	public ChannelResolver(ClosableObservable<Collection<PV>> source) {
		setSource(source);
	}

	public ChannelType<String> get(String address) {	
		return ChannelTypeLookup.get(getType(address));
	}
	
	@Override
	protected ChannelResolver transform(Collection<PV> value) {
		lookup.clear();
		for (PV pv : value) {
			lookup.put(pv.getAddress(), pv);
		}
		
		return this;
	}
	
	private String getType(String address) {
		if (lookup.containsKey(address)) {
			return lookup.get(address).type();
		}
		
		return "";
	}
}
