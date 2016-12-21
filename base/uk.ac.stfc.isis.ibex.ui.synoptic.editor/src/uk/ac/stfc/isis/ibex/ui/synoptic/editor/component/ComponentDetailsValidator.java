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
package uk.ac.stfc.isis.ibex.ui.synoptic.editor.component;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.IO;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;
import uk.ac.stfc.isis.ibex.validators.PvValidator;

/**
 *
 */
public class ComponentDetailsValidator extends ErrorMessageProvider {

    private static final String NON_UNIQUE_PV_ERROR = "%s: %s (%s) PV is not unique";

    private ComponentDescription comp;
    private String compName;

    private PropertyChangeListener pvValidator = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("pvChanged") || evt.getPropertyName().equals("pvRemoved")) {
                validatePvList();
            }
        }
    };

    public ComponentDetailsValidator(ComponentDescription comp) {
        this.comp = comp;

        comp.addPropertyChangeListener(pvValidator);

        comp.addPropertyChangeListener("name", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                compName = (String) evt.getNewValue();
                validatePvList();
            }
        });

        comp.addPropertyChangeListener("pvAdded", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                PV newPV = (PV) evt.getNewValue();
                newPV.addPropertyChangeListener(pvValidator);
            }
        });

        for (PV pv : comp.pvs()) {
            pv.addPropertyChangeListener(pvValidator);
        }
    }

    private void validatePvList() {
        LinkedList<PV> pvs = new LinkedList<>(comp.pvs());

        while (pvs.size() > 0) {
            PV pv = pvs.pop();

            String name = pv.displayName();
            IO mode = pv.recordType().io();
            String address = pv.address();

            for (PV otherPv : pvs) {
                if (otherPv.displayName().equals(pv.displayName())) {
                    if (otherPv.recordType().io().equals(pv.recordType().io())) {
                        setError(true, String.format(NON_UNIQUE_PV_ERROR, compName, name, mode));
                        return;
                    }
                }
            }

            PvValidator addressValidator = new PvValidator();
            boolean addressValid = addressValidator.validatePvAddress(address);
            if (!addressValid) {
                setError(true, addressValidator.getErrorMessage());
                return;
            }
        }
        setError(false, null);
    }
}
