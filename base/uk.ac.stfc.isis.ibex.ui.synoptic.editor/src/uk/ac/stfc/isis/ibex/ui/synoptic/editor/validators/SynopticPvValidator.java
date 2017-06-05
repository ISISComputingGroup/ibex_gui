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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;
import uk.ac.stfc.isis.ibex.validators.PvValidator;

/**
 * This validator will check the address for a single PV.
 */
public class SynopticPvValidator extends ErrorMessageProvider {
    private PvValidator validator = new PvValidator();
    
    /**
     * Adds listeners on to a pv so that it can be set to error when it changes.
     * 
     * @param pv
     *            The pv to listen to.
     */
    public SynopticPvValidator(final PV pv) {
        pv.addPropertyChangeListener("pvAddress", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String address = (String) evt.getNewValue();

                if (validator.validatePvAddress(address)) {
                    setError(false, null);
                } else {
                    String error = constructError(pv.displayName());
                    setError(true, error);
                }

            }
        });

        pv.addPropertyChangeListener("pvName", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (getError().isError()) {
                    setError(true, constructError((String) evt.getNewValue()));
                }
            }
        });
    }

    private String constructError(String pvName) {
        StringBuilder b = new StringBuilder();
        b.append(pvName + ": ");
        b.append(validator.getErrorMessage());
        return b.toString();
    }

}
