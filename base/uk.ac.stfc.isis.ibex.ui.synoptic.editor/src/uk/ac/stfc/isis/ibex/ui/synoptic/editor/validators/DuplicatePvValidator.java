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
package uk.ac.stfc.isis.ibex.ui.synoptic.editor.validators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;

/**
 * This validator will check the list of PVs for any duplicates.
 */
public class DuplicatePvValidator extends ErrorMessageProvider { 

    /**
     * Check that no PVs have the same name and type.
     * @param pvs The list of all PVs on this component to check.
     */
    public void checkForDuplicatePVs(List<PV> pvs) {
        Set<Integer> uniquePvs = new HashSet<>();
        List<String> duplicatePVNames = new ArrayList<>();
        
        for (PV pv: pvs) {
            Integer nameAndTypeHash = pv.displayName().trim().hashCode() ^ pv.recordType().io().hashCode();
            if (!uniquePvs.add(nameAndTypeHash)) {
                duplicatePVNames.add(pv.displayName());
            }
        }
        
        if (!duplicatePVNames.isEmpty()) {
            setError(true, constructError(duplicatePVNames));
        } else {
            setError(false, null);
        }
    }
    
    private String constructError(Collection<String> duplicates) {
        StringBuilder b = new StringBuilder();
        for (String duplicate : duplicates) {
            b.append("PV name '" + duplicate + "' is repeated.");
        }
        return b.toString();
    }

}
