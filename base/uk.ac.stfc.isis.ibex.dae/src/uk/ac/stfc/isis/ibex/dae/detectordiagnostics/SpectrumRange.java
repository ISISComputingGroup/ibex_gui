 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class SpectrumRange {
    
    public List<Integer> spectra = new ArrayList<>();
    
    public List<Integer> pagesRequired() {
        List<Integer> pagesRequired = new ArrayList<>();
        
        pagesRequired.add(0);
        
        for (Integer spectrum : spectra) {
            
            Integer page = Math.floorDiv(spectrum, 1000);
            
            if (!pagesRequired.contains(page)) {
                pagesRequired.add(page);
            }
        }
        
        return pagesRequired;
    }
    
}
