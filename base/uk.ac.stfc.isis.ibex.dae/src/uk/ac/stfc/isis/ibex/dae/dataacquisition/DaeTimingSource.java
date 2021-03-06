
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

package uk.ac.stfc.isis.ibex.dae.dataacquisition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Enum containing the possible timing sources for the DAE.
 */
public enum DaeTimingSource {
    /**
     * The isis timing source.
     */
    ISIS("ISIS"),
    /**
     * The internal test clock timing source.
     */
    INTERNAL_TEST_CLOCK("Internal Test Clock"),
    /**
     * The SMP (secondary master pulse) timing source. This source is usually a
     * chopper plugged into the SMP socket.
     */
    SMP("SMP"),
    /**
     * The Muon Cerenkov timing source.
     */
    MUON_CERENKOV("Muon Cerenkov"),
    /**
     * The muon MS timing source.
     */
    MUON_MS("Muon MS"),
    /**
     * The ISIS timing source (first TS1 pulse).
     */
    ISIS_FIRST_TS1("ISIS (first TS1)"),
    /**
     * The ISIS timing source (TS1 pulses only).
     */
    ISIS_TS1_ONLY("ISIS (TS1 only)");

	private String text;

	private static final List<String> ALLTOSTRING;
	static {
		ALLTOSTRING = new ArrayList<>();
		for (DaeTimingSource method : DaeTimingSource.values()) {
			ALLTOSTRING.add(method.toString());
		}
	}
	
    /**
     * Defines a singular timing source with it's human readable name.
     * 
     * @param text
     *            The human readable name of the timing source.
     */
	DaeTimingSource(String text) {
		this.text = text;
	}

    /**
     * Get an array of all the possible timing sources.
     * 
     * @return An array of strings containing the possible timing sources.
     */
	public static List<String> allToString() {
		return Collections.unmodifiableList(ALLTOSTRING);
	}
	
	@Override
	public String toString() {
		return text;
	}
}
