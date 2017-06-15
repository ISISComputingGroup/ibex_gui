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
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;
import uk.ac.stfc.isis.ibex.validators.ErrorAggregator;

/**
 * This validator will check that the whole synoptic is valid.
 */
public class SynopticValidator extends ErrorAggregator {
    private ComponentListValidator listValidator;
    private Map<ComponentDescription, ComponentValidator> compValidators = new HashMap<>();

    /**
     * Adds listeners on to the synoptic to know when there is an error.
     * 
     * @param synoptic
     *            The synoptic to validate.
     */
    public SynopticValidator(final SynopticDescription synoptic) {
        listValidator = new ComponentListValidator(synoptic);

        listValidator.addPropertyChangeListener("error", errorListener);
        
        for (ComponentDescription comp : synoptic.components()) {
            addComponent(comp);
        }

        synoptic.addPropertyChangeListener("componentAdded", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                ComponentDescription comp = (ComponentDescription) evt.getNewValue();
                addComponent(comp);
            }
        });

        synoptic.addPropertyChangeListener("componentRemoved", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                compValidators.remove(evt.getNewValue());
            }
        });
    }

    private void addComponent(ComponentDescription comp) {
        ComponentValidator validator = new ComponentValidator(comp);
        compValidators.put(comp, validator);
        validator.addPropertyChangeListener("error", errorListener);
    }

    @Override
    public String constructMessage() {
        StringBuilder sb = new StringBuilder();
        for (String c : getErrorMessages()) {
            sb.append(c);
            sb.append("; ");
        }
        sb.deleteCharAt(sb.length() - 2);
        return sb.toString();
    };
}
