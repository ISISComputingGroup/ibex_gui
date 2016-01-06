
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

package uk.ac.stfc.isis.ibex.instrument.pv;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import uk.ac.stfc.isis.ibex.epics.observing.BaseCachingObservable;

/**
 * This class is responsible for maintaining an address book of PVs being accessed 
 *
 */
public class PVAddressBook {
	
	private String prefix;
	private final Map<String, PVAddress> addresses = new ConcurrentHashMap<>();
	
	public PVAddressBook(String prefix) {
		this.prefix = prefix;
	}

	public BaseCachingObservable<String> resolvePV(String suffix) {
		if (addresses.containsKey(suffix)) {
			return addresses.get(suffix);
		}
		
		PVAddress address = new PVAddress(prefix, suffix);		
		addresses.put(suffix, address);
		return address;
	}
	
    public BaseCachingObservable<String> resolvePV(String suffix, PVType type) {
		if (addresses.containsKey(suffix)) {
			return addresses.get(suffix);
		}
		
		PVAddress address = null;
		if (type == PVType.REMOTE_PV){
			address = new PVAddress("", suffix);
		}
		else {
			address = new PVAddress(prefix, suffix);
		}
		addresses.put(suffix, address);
		return address;
	}
	
	public void setPrefix(String newPrefix) {
		this.prefix = newPrefix;
		for (PVAddress address : addresses.values()) {
			address.setPrefix(newPrefix);
		}			
	}
}
