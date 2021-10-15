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
import java.util.HashMap;
import java.util.Map;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;
import uk.ac.stfc.isis.ibex.validators.ErrorAggregator;

/**
 * This validator will check that all the pvs in a component are valid.
 */
public class ComponentValidator extends ErrorAggregator {
    private ComponentDescription component;

    // Had to use the identity hash code here as the overridden one in PV
    // doesn't work
    private Map<Integer, SynopticPvValidator> validators = new HashMap<>();    
    private DuplicatePvValidator duplicatePVNameValidator = new DuplicatePvValidator();
    
    /**
     * Default constructor. Adds change listeners to the component.
     * 
     * @param component
     *            The component to validate
     */
    public ComponentValidator(ComponentDescription component) {
        this.component = component;
        duplicatePVNameValidator.addPropertyChangeListener("error", errorListener);
        
        for (PV pv : component.pvs()) {
            addPVListener(pv);
        }
        
        duplicatePVNameValidator.checkForDuplicatePVs(component.pvs());

        component.addPropertyChangeListener("pvAdded", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                PV pv = (PV) evt.getNewValue();
                addPVListener(pv);
                duplicatePVNameValidator.checkForDuplicatePVs(component.pvs());
            }
        });

        component.addPropertyChangeListener("pvRemoved", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                int hash = System.identityHashCode(evt.getNewValue());
                childErrors.remove(validators.get(hash));
                validators.remove(evt.getNewValue());
                duplicatePVNameValidator.checkForDuplicatePVs(component.pvs());
                updateErrors();
            }
        });

        component.addPropertyChangeListener("componentName", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (getError().isError()) {
                    setError(true, constructMessage());
                }
            }
        });
        
    }
    
    private void addPVListener(PV pv) {
        SynopticPvValidator pvValid = new SynopticPvValidator(pv);
        validators.put(System.identityHashCode(pv), pvValid);
        pv.addPropertyChangeListener((PropertyChangeEvent evt) -> duplicatePVNameValidator.checkForDuplicatePVs(component.pvs()));
        pvValid.addPropertyChangeListener("error", errorListener);
    }

    @Override
    public String constructMessage() {
        StringBuilder sb = new StringBuilder();
        for (String c : getErrorMessages()) {
            sb.append("In " + component.name());
            sb.append(", " + c);
            sb.append("; ");
        }
        sb.deleteCharAt(sb.length() - 2);
        return sb.toString();
    }

}
