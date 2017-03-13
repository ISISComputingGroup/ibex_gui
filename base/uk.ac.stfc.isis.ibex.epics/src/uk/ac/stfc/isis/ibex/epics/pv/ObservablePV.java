
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

package uk.ac.stfc.isis.ibex.epics.pv;

import java.util.logging.Logger;

import org.epics.vtype.VType;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;

/**
 * A class for observing an EPICS process variable.
 *
 * @param <T> the PV type (must be a VType)
 */
public abstract class ObservablePV<T extends VType> extends ClosableObservable<T> implements PV<T> {

	private final PVInfo<T> info;

	public ObservablePV(PVInfo<T> info) {
        if (info.address().contains("BLOCKSERVER:IOCS")) {
            Logger.getGlobal().info("(Ticket 2161) ObservablePV initialized looking at " + info.address());
        }
		this.info = info;
	}
	
	@Override
	public PVInfo<T> details() {
		return info;
	}
}
