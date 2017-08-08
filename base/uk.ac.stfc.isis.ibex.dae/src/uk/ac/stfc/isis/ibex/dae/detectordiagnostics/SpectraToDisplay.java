 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2017 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.dae.detectordiagnostics;

/**
 * A list of options of spectra to display.
 */
public enum SpectraToDisplay {
    /**
     * Display all spectra.
     */
    ALL("All"),
    /**
     * Display only spectra with zero counts.
     */
    ZERO_COUNTS_ONLY("Zero Counts Only"),
    /**
     * Display only spectra with non-zero counts.
     */
    NON_ZERO_COUNTS_ONLY("Non-zero counts only");
    
    private String displayName;
    
    /**
     * Constructor.
     * 
     * @param displayName the display name used in e.g. dropdown menus
     */
    SpectraToDisplay(String displayName) {
        this.displayName = displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
