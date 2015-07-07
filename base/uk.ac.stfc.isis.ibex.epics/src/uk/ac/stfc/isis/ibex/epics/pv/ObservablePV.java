
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

import org.epics.vtype.VType;

import uk.ac.stfc.isis.ibex.epics.observing.BaseCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;


/**
 * A class for observing an EPICS process variable.
 *
 * @param <T> the PV type (must be a VType)
 */
public abstract class ObservablePV<T extends VType> extends BaseCachingObservable<T> implements PV<T>, ClosableCachingObservable<T> {

	private final PVInfo<T> info;

	public ObservablePV(PVInfo<T> info) {
		this.info = info;
	}
	
	@Override
	public PVInfo<T> details() {
		return info;
	}
}
